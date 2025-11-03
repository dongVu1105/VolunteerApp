package com.dongVu1105.event_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@Builder
@Document("event")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @MongoId
    String id;
    String title;
    String location;
    String description;
    Instant startDate;
    Instant finishDate;
    String category;
    String managerId;
    Instant createdDate;
    String image;
    boolean statusEvent;
}
