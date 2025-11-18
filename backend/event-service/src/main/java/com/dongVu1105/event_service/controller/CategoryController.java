package com.dongVu1105.event_service.controller;

import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.response.CategoryResponse;
import com.dongVu1105.event_service.service.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {
    CategoryService categoryService;

    // Lấy danh sách danh mục
    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponse>> findAll (){
        return ApiResponse.<List<CategoryResponse>>builder().data(categoryService.findAll()).build();
    }
}
