package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.entity.React;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReactMapper {
    @Mapping(target = "createdDate", ignore = true)
    ReactResponse toReactResponse (React react);
}
