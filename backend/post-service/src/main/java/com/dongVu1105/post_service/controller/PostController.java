package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.request.EventRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;
    ObjectMapper objectMapper;

    // Tạo bài viết trong sự kiện
    @PostMapping("/create")
    public ApiResponse<PostResponse> create (
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        PostRequest request = objectMapper.readValue(requestJson, PostRequest.class);
        return ApiResponse.<PostResponse>builder().data(postService.create(file, request)).build();
    }

    // Xóa 1 bài viết trong sự kiện
    @DeleteMapping("/delete/{postId}")
    public ApiResponse<Void> delete (@PathVariable("postId") String postId){
        postService.delete(postId);
        return ApiResponse.<Void>builder().build();
    }

    // Tìm tất cả bài viết trong sự kiện
    @GetMapping("/find-all")
    public ApiResponse<PageResponse<PostResponse>> findAllByEventId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "eventId") String eventId){
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .data(postService.findAllByEventId(page, size, eventId)).build();
    }

    // Tìm bài viết theo id
    @GetMapping("/{id}")
    public ApiResponse<PostResponse> findById (@PathVariable("id") String id){
        return ApiResponse.<PostResponse>builder().data(postService.findById(id)).build();
    }
}
