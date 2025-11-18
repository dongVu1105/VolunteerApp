package com.dongVu1105.post_service.mapper;

import com.dongVu1105.post_service.dto.request.ChildCommentRequest;
import com.dongVu1105.post_service.dto.request.CommentRequest;
import com.dongVu1105.post_service.dto.response.ChildCommentResponse;
import com.dongVu1105.post_service.dto.response.CommentResponse;
import com.dongVu1105.post_service.entity.ChildComment;
import com.dongVu1105.post_service.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ChildCommentMapper {
    ChildComment toChildComment (ChildCommentRequest request);
    @Mapping(target = "createdDate", ignore = true)
    ChildCommentResponse toChildCommentResponse (ChildComment childComment);
}
