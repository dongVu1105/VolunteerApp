package com.dongVu1105.notification_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EventNoti {
    String eventId;
    String eventTitle;
    String receiverId;
}
