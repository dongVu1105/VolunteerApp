package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.response.ChildReactResponse;
import com.dongVu1105.post_service.dto.response.ReactResponse;
import com.dongVu1105.post_service.entity.ChildReact;
import com.dongVu1105.post_service.entity.React;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChildReactMapper {
    @Mapping(target = "createdDate", ignore = true)
    ChildReactResponse toChildReactResponse (ChildReact childReact);
}
