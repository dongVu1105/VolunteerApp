package com.dongVu1105.event_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class EventUserNoti {
    String eventUserId;
    String username;
    String eventTitle;
    String receiverId;
}
