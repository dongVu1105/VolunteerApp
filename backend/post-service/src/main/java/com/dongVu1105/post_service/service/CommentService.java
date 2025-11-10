package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.request.ReactNoti;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.dto.response.EventResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.entity.Comment;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.CommentMapper;
import com.dongVu1105.post_service.mapper.PostMapper;
import com.dongVu1105.post_service.repository.CommentRepository;
import com.dongVu1105.post_service.repository.PostRepository;
import com.dongVu1105.post_service.repository.httpClient.EventClient;
import com.dongVu1105.post_service.repository.httpClient.FileClient;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CommentService {
    PostRepository postRepository;
    DateTimeFormatter dateTimeFormatter;
    EventClient eventClient;
    FileClient fileClient;
    CommentMapper commentMapper;
    CommentRepository commentRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public CommentResponse create (MultipartFile file, CommentRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String managerId = verify(post.getEventId(), userId);
        String imageURL = fileClient.uploadMedia(file).getData().getUrl();
        Comment comment = commentMapper.toComment(request);
        comment.setImageURL(imageURL);
        comment.setCreatedDate(Instant.now());
        comment.setOwnerId(userId);
        comment = commentRepository.save(comment);
        kafkaTemplate.send("comment", ReactNoti.builder()
                .managerId(managerId)
                .creator(userId)
                .ownerId(post.getOwnerId())
                .eventId(post.getEventId())
                .postId(post.getId())
                .build());
        return toCommentResponse(comment);
    }

    public void delete (String commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_EXISTED));
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EventResponse eventResponse = eventClient.findById(post.getEventId()).getData();
        if(Objects.isNull(eventResponse)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!(post.getOwnerId().equals(userId) || eventResponse.getManagerId().equals(userId)
                || comment.getOwnerId().equals(userId))){
            throw new AppException(ErrorCode.CANNOT_COMMENT);
        }
        commentRepository.deleteById(commentId);
    }

    private String verify (String eventId, String userId){
        EventResponse eventResponse = eventClient.findById(eventId).getData();
        Boolean isUserInEvent = eventClient.isUserInEvent(userId, eventId).getData();
        if(Objects.isNull(eventResponse) || Objects.isNull(isUserInEvent)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!(isUserInEvent || eventResponse.getManagerId().equals(userId))){
            throw new AppException(ErrorCode.CANNOT_COMMENT);
        }
        return eventResponse.getManagerId();
    }

    public PageResponse<CommentResponse> findAllByPostId (int page, int size, String postId){
        postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Comment> commentPage = commentRepository.findAllByPostId(postId, pageable);
        var commentData = commentPage.getContent().stream().map(this::toCommentResponse).toList();
        return PageResponse.<CommentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(commentPage.getTotalPages())
                .totalElements(commentPage.getTotalElements())
                .result(commentData).build();
    }

    private CommentResponse toCommentResponse (Comment comment){
        CommentResponse commentResponse = commentMapper.toCommentResponse(comment);
        commentResponse.setCreatedDate(dateTimeFormatter.format(comment.getCreatedDate()));
        return commentResponse;
    }
}
