package com.dongVu1105.post_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String managerUsername;
    String createdDate;
    String image;
    boolean statusEvent;
}
