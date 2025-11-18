package com.dongVu1105.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChildReactResponse {
    String id;
    String commentId;
    String ownerId;
    String ownerAvatar;
    String ownerUsername;
    String createdDate;
}
