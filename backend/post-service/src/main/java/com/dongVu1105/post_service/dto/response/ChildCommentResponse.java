package com.dongVu1105.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChildCommentResponse {
    String id;
    String commentId;
    String ownerId;
    String ownerUsername;
    String ownerAvatar;
    String text;
    String imageURL;
    String createdDate;
}
