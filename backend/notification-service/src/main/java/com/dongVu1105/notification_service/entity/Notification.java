package com.dongVu1105.notification_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("notification")
public class Notification <T> {
    @MongoId
    String id;
    String subject;
    String message;
    List<String> receiverId;
    T info;
    Instant createdDate;
    boolean isRead;
}
