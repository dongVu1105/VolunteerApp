package com.dongVu1105.post_service.service;

import com.dongVu1105.post_service.dto.request.PostNoti;
import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.EventResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.dto.response.UserProfileResponse;
import com.dongVu1105.post_service.entity.Post;
import com.dongVu1105.post_service.exception.AppException;
import com.dongVu1105.post_service.exception.ErrorCode;
import com.dongVu1105.post_service.mapper.PostMapper;
import com.dongVu1105.post_service.repository.CommentRepository;
import com.dongVu1105.post_service.repository.PostRepository;
import com.dongVu1105.post_service.repository.ReactRepository;
import com.dongVu1105.post_service.repository.httpClient.EventClient;
import com.dongVu1105.post_service.repository.httpClient.FileClient;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PostService {
    PostRepository postRepository;
    PostMapper postMapper;
    DateTimeFormatter dateTimeFormatter;
    EventClient eventClient;
    FileClient fileClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    UserProfileClient userProfileClient;
    ReactService reactService;
    CommentService commentService;
    ReactRepository reactRepository;
    CommentRepository commentRepository;

    public PostResponse create (MultipartFile file, PostRequest request){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String managerId = verify(request.getEventId(), userId);
        Post post = postMapper.toPost(request);
        String imageURL = fileClient.uploadMedia(file).getData().getUrl();
        post.setImageURL(imageURL);
        post.setCreatedDate(Instant.now());
        post.setOwnerId(userId);
        post = postRepository.save(post);
        List<String> receiverId = new ArrayList<>();
        receiverId.add(managerId);
        UserProfileResponse userProfileResponse = userProfileClient.findById(userId).getData();
        if(Objects.isNull(userProfileResponse)){
            log.error(ErrorCode.UNCONNECTED_SERVICE.getMessage());
        }
        kafkaTemplate.send("post", PostNoti.builder()
                .postId(post.getId())
                .creatorName(userProfileResponse.getUsername())
                .creatorId(userId)
                .eventId(post.getEventId())
                .receiverId(receiverId)
                .build());
        return this.toPostResponse(post, userProfileResponse);
    }

    public PostResponse findById (String id){
        Post post = postRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponse userProfileResponse = userProfileClient.findById(userId).getData();
        if(Objects.isNull(userProfileResponse)){
            log.error(ErrorCode.UNCONNECTED_SERVICE.getMessage());
        }
        return toPostResponse(post, userProfileResponse);
    }

    public void delete (String postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        EventResponse eventResponse = eventClient.findById(post.getEventId()).getData();
        if(Objects.isNull(eventResponse)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!(post.getOwnerId().equals(userId) || eventResponse.getManagerId().equals(userId))){
            throw new AppException(ErrorCode.CANNOT_POST);
        }
        postRepository.deleteById(postId);
    }

    public PageResponse<PostResponse> findAllByEventId (int page, int size, String eventId){
        EventResponse eventResponse = eventClient.findById(eventId).getData();
        if(Objects.isNull(eventResponse)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if (!eventResponse.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Post> postPage = postRepository.findAllByEventId(eventId, pageable);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileResponse userProfileResponse = userProfileClient.findById(userId).getData();
        if(Objects.isNull(userProfileResponse)){
            log.error(ErrorCode.UNCONNECTED_SERVICE.getMessage());
        }
        var postData = postPage.getContent().stream()
                .map(post -> this.toPostResponse(post, userProfileResponse)).toList();
        return PageResponse.<PostResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .hasPreviousPage(postPage.hasPrevious())
                .hasNextPage(postPage.hasNext())
                .result(postData).build();
    }

    private String verify (String eventId, String userId){
        EventResponse eventResponse = eventClient.findById(eventId).getData();
        Boolean isUserInEvent = eventClient.isUserInEvent(userId, eventId).getData();
        if(Objects.isNull(eventResponse) || Objects.isNull(isUserInEvent)){
            throw new AppException(ErrorCode.EVENT_NOT_EXISTED);
        }
        if(!eventResponse.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        if (!(isUserInEvent || eventResponse.getManagerId().equals(userId))){
            throw new AppException(ErrorCode.CANNOT_POST);
        }
        return eventResponse.getManagerId();
    }



    private PostResponse toPostResponse (Post post, UserProfileResponse userProfileResponse){
        PostResponse postResponse = postMapper.toPostResponse(post);
        postResponse.setCreatedDate(dateTimeFormatter.format(post.getCreatedDate()));
        postResponse.setOwnerUsername(userProfileResponse.getUsername());
        postResponse.setOwnerAvatar(userProfileResponse.getAvatar());
        postResponse.setReactedByCurrentUser(reactRepository
                .existsByPostIdAndOwnerId(postResponse.getId(), userProfileResponse.getUserId()));
        postResponse.setReactCount(reactRepository.countByPostId(postResponse.getId()));
        postResponse.setCommentCount(commentRepository.countByPostId(postResponse.getId()));
        return postResponse;
    }
}
