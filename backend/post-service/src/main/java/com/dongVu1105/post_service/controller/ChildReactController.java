package com.dongVu1105.post_service.controller;

import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.response.ChildReactResponse;
import com.dongVu1105.post_service.dto.response.PageResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.service.ChildReactService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/child-react")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChildReactController {
    ChildReactService childReactService;

    @PostMapping("/create/{commentId}")
    public ApiResponse<ChildReactResponse> create (@PathVariable("commentId") String commentId) {
        return ApiResponse.<ChildReactResponse>builder().data(childReactService.create(commentId)).build();
    }

    @DeleteMapping("/delete/{child-reactId}")
    public ApiResponse<Void> delete (@PathVariable("child-reactId") String childReactId){
        childReactService.delete(childReactId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/find-by-commentId")
    public ApiResponse<PageResponse<ChildReactResponse>> findAllByCommentId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "commentId") String commentId){
        return ApiResponse.<PageResponse<ChildReactResponse>>builder()
                .data(childReactService.findAllByCommentId(page, size, commentId)).build();
    }
}
