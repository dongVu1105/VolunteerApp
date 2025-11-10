package com.dongVu1105.post_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(value = "post")
public class Post {
    @MongoId
    String id;
    String eventId;
    String ownerId;
    String text;
    String imageURL;
    Instant createdDate;
}
