package com.dongVu1105.event_service.mapper;

import com.dongVu1105.event_service.dto.response.CategoryResponse;
import com.dongVu1105.event_service.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse (Category category);
}
