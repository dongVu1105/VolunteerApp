package com.dongVu1105.notification_service.mapper;

import com.dongVu1105.notification_service.dto.response.NotificationResponse;
import com.dongVu1105.notification_service.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "createdDate", ignore = true)
    NotificationResponse toNotificationResponse (Notification notification);
}
