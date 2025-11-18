package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.PostNoti;
import com.dongVu1105.post_service.dto.response.*;
import com.dongVu1105.post_service.entity.ChildReact;
import com.dongVu1105.post_service.entity.Comment;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.ChildReactMapper;
import com.dongVu1105.post_service.repository.*;
import com.dongVu1105.post_service.repository.httpClient.EventClient;
import com.dongVu1105.post_service.repository.httpClient.UserProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChildReactService {
    PostRepository postRepository;
    DateTimeFormatter dateTimeFormatter;
    EventClient eventClient;
    ChildReactMapper childReactMapper;
    ChildCommentRepository childCommentRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    UserProfileClient userProfileClient;
    CommentRepository commentRepository;
    ChildReactRepository childReactRepository;

    public ChildReactResponse create (String commentId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        verify(post.getEventId(), userId);
        ChildReact childReact = ChildReact.builder()
                .createdDate(Instant.now())
                .ownerId(userId)
                .commentId(comment.getId())
                .build();
        childReact = childReactRepository.save(childReact);
        List<String> receiverId = new ArrayList<>();
        receiverId.add(post.getOwnerId());
        receiverId.add(comment.getOwnerId());
        UserProfileResponse userProfileResponse = userProfileClient.findById(userId).getData();
        if(Objects.isNull(userProfileResponse)){
            log.error(ErrorCode.UNCONNECTED_SERVICE.getMessage());
        }
        kafkaTemplate.send("react-of-comment", PostNoti.builder()
                .postId(post.getId())
                .creatorName(userProfileResponse.getUsername())
                .creatorId(userId)
                .eventId(post.getEventId())
                .receiverId(receiverId)
                .build());
        return toChildReactResponse(childReact, userProfileResponse);
    }

    public void delete (String childReactId){
        ChildReact childReact = childReactRepository.findById(childReactId)
                .orElseThrow(() -> new AppException(ErrorCode.REACT_NOT_EXISTED));
        Comment comment = commentRepository.findById(childReact.getCommentId())
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EventResponse eventResponse = eventClient.findById(post.getEventId()).getData();
        if(Objects.isNull(eventResponse)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!childReact.getOwnerId().equals(userId)){
            throw new AppException(ErrorCode.CANNOT_REACT);
        }
        childReactRepository.deleteById(childReactId);
    }

    public PageResponse<ChildReactResponse> findAllByCommentId (int page, int size, String commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ChildReact> childReactPage = childReactRepository.findAllByCommentId(commentId, pageable);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponse userProfileResponse = userProfileClient.findById(userId).getData();
        if(Objects.isNull(userProfileResponse)){
            log.error(ErrorCode.UNCONNECTED_SERVICE.getMessage());
        }
        var childReactData = childReactPage.getContent().stream()
                .map(childReact -> this.toChildReactResponse(childReact, userProfileResponse)).toList();
        return PageResponse.<ChildReactResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(childReactPage.getTotalPages())
                .totalElements(childReactPage.getTotalElements())
                .hasPreviousPage(childReactPage.hasPrevious())
                .hasNextPage(childReactPage.hasNext())
                .result(childReactData).build();
    }

    private String verify (String eventId, String userId){
        EventResponse eventResponse = eventClient.findById(eventId).getData();
        Boolean isUserInEvent = eventClient.isUserInEvent(userId, eventId).getData();
        if(Objects.isNull(eventResponse) || Objects.isNull(isUserInEvent)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!(isUserInEvent || eventResponse.getManagerId().equals(userId))){
            throw new AppException(ErrorCode.CANNOT_REACT);
        }
        return eventResponse.getManagerId();
    }

    private ChildReactResponse toChildReactResponse (ChildReact childReact, UserProfileResponse userProfileResponse){
        ChildReactResponse childReactResponse = childReactMapper.toChildReactResponse(childReact);
        childReactResponse.setCreatedDate(dateTimeFormatter.format(childReact.getCreatedDate()));
        childReactResponse.setOwnerUsername(userProfileResponse.getUsername());
        childReactResponse.setOwnerAvatar(userProfileResponse.getAvatar());
        return childReactResponse;
    }
}
