package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.ReactNoti;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.dto.response.EventResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.entity.Comment;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.entity.React;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.CommentMapper;
import com.dongVu1105.post_service.mapper.ReactMapper;
import com.dongVu1105.post_service.repository.CommentRepository;
import com.dongVu1105.post_service.repository.PostRepository;
import com.dongVu1105.post_service.repository.ReactRepository;
import com.dongVu1105.post_service.repository.httpClient.EventClient;
import com.dongVu1105.post_service.repository.httpClient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactService {
    PostRepository postRepository;
    DateTimeFormatter dateTimeFormatter;
    EventClient eventClient;
    ReactMapper reactMapper;
    ReactRepository reactRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public ReactResponse create (String postId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String managerId = verify(post.getEventId(), userId);
        React react = React.builder()
                .createdDate(Instant.now())
                .ownerId(userId)
                .postId(postId)
                .build();
        react = reactRepository.save(react);
        kafkaTemplate.send("react", ReactNoti.builder()
                .managerId(managerId)
                .creator(userId)
                .ownerId(post.getOwnerId())
                .eventId(post.getEventId())
                .postId(post.getId())
                .build());
        return toReactResponse(react);
    }

    public void delete (String reactId){
        React react = reactRepository.findById(reactId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_REACT));
        Post post = postRepository.findById(react.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EventResponse eventResponse = eventClient.findById(post.getEventId()).getData();
        if(Objects.isNull(eventResponse)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!react.getOwnerId().equals(userId)){
            throw new AppException(ErrorCode.CANNOT_COMMENT);
        }
        reactRepository.deleteById(reactId);
    }

    public PageResponse<ReactResponse> findAllByPostId (int page, int size, String postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<React> reactPage = reactRepository.findAllByPostId(postId, pageable);
        var reactData = reactPage.getContent().stream().map(this::toReactResponse).toList();
        return PageResponse.<ReactResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(reactPage.getTotalPages())
                .totalElements(reactPage.getTotalElements())
                .result(reactData).build();
    }

    public boolean isReacted (String postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return reactRepository.existsByPostIdAndOwnerId(postId, userId);
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

    private ReactResponse toReactResponse (React react){
        ReactResponse reactResponse = reactMapper.toReactResponse(react);
        reactResponse.setCreatedDate(dateTimeFormatter.format(react.getCreatedDate()));
        return reactResponse;
    }
}
