package com.dongVu1105.event_service.service;

import com.dongVu1105.event_service.dto.response.CategoryResponse;
import com.dongVu1105.event_service.mapper.CategoryMapper;
import com.dongVu1105.event_service.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll (){
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
    }
}
