package com.dongVu1105.event_service.dto.request;

import com.dongVu1105.event_service.validation.StartDateConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdationRequest {
    String id;
    @Size(min = 5, message = "INVALID_TITLE")
    String title;
    @NotNull(message = "FIELD_REQUIRED")
    String location;
    @NotNull(message = "FIELD_REQUIRED")
    String description;
    @NotNull(message = "FIELD_REQUIRED")
    @StartDateConstraint(min = 1, message = "INVALID_STARTDATE")
    Instant startDate;
    @NotNull(message = "FIELD_REQUIRED")
    Instant finishDate;
    @NotNull(message = "FIELD_REQUIRED")
    String category;
}
