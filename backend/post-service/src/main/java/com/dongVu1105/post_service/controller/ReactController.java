package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.service.CommentService;
import com.dongVu1105.post_service.service.ReactService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/react")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactController {
    ReactService reactService;
    ObjectMapper objectMapper;

    // Thích một bài viết
    @PostMapping("/create/{postId}")
    public ApiResponse<ReactResponse> create (@PathVariable("postId") String postId) {
        return ApiResponse.<ReactResponse>builder().data(reactService.create(postId)).build();
    }

    // Hủy thích 1 bài viết
    @DeleteMapping("/delete/{reactId}")
    public ApiResponse<Void> delete (@PathVariable("reactId") String reactId){
        reactService.delete(reactId);
        return ApiResponse.<Void>builder().build();
    }

    // Tìm tất cả lượt thích trong bài viết
    @GetMapping("/find-by-postId")
    public ApiResponse<PageResponse<ReactResponse>> findAllByPostId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "postId") String postId){
        return ApiResponse.<PageResponse<ReactResponse>>builder()
                .data(reactService.findAllByPostId(page, size, postId)).build();
    }
}
