package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {
    CommentService commentService;
    ObjectMapper objectMapper;

    @PostMapping("/create")
    public ApiResponse<CommentResponse> create (
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        CommentRequest request = objectMapper.readValue(requestJson, CommentRequest.class);
        return ApiResponse.<CommentResponse>builder().data(commentService.create(file, request)).build();
    }

    @DeleteMapping("/delete/{commentId}")
    public ApiResponse<Void> delete (@PathVariable("commentId") String commentId){
        commentService.delete(commentId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/find-by-postId")
    public ApiResponse<PageResponse<CommentResponse>> findAllByPostId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "postId") String postId){
        return ApiResponse.<PageResponse<CommentResponse>>builder()
                .data(commentService.findAllByPostId(page, size, postId)).build();
    }
}
