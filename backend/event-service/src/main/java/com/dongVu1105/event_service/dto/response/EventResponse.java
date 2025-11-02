package com.dongVu1105.event_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {
    String id;
    String title;
    String location;
    String description;
    Instant startDate;
    Instant finishDate;
    String category;
    String managerId;
    String createdDate;
    String image;
    boolean statusEvent;
}
