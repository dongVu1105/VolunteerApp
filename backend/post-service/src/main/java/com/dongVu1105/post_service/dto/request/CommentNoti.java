package com.dongVu1105.post_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentNoti {
    String postId;
    String managerId;
    String ownerId;
    String eventId;
    String creator;
}
