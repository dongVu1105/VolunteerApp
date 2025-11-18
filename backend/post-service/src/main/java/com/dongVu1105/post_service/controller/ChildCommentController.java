package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.request.ChildCommentRequest;
import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.ChildCommentResponse;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.service.ChildCommentService;
import com.dongVu1105.post_service.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/child-comment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChildCommentController {
    ChildCommentService childCommentService;
    ObjectMapper objectMapper;

    @PostMapping("/create")
    public ApiResponse<ChildCommentResponse> create (
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        ChildCommentRequest request = objectMapper.readValue(requestJson, ChildCommentRequest.class);
        return ApiResponse.<ChildCommentResponse>builder().data(childCommentService.create(file, request)).build();
    }

    @DeleteMapping("/delete/{child-commentId}")
    public ApiResponse<Void> delete (@PathVariable("child-commentId") String childCommentId){
        childCommentService.delete(childCommentId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/find-by-commentId")
    public ApiResponse<PageResponse<ChildCommentResponse>> findAllByCommentId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "commentId") String commentId){
        return ApiResponse.<PageResponse<ChildCommentResponse>>builder()
                .data(childCommentService.findAllByCommentId(page, size, commentId)).build();
    }
}
