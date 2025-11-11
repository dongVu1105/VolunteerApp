package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment toComment (CommentRequest request);
    @Mapping(target = "createdDate", ignore = true)
    CommentResponse toCommentResponse (Comment comment);
}
