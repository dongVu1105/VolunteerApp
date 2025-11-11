package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.request.PostRequest;
import com.dongVu1105.post_service.dto.response.PostResponse;
import com.dongVu1105.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post toPost (PostRequest request);
    @Mapping(target = "createdDate", ignore = true)
    PostResponse toPostResponse (Post post);
}
