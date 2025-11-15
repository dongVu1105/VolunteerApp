package com.dongVu1105.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String postId;
    String ownerId;
    String ownerUsername;
    String ownerAvatar;
    String text;
    String imageURL;
    String createdDate;
}
