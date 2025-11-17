package com.dongVu1105.notification_service.controller;

import com.dongVu1105.notification_service.dto.ApiResponse;
import com.dongVu1105.notification_service.dto.response.NotificationResponse;
import com.dongVu1105.notification_service.dto.response.PageResponse;
import com.dongVu1105.notification_service.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping("/find-all")
    public ApiResponse<PageResponse<NotificationResponse>> findAllByUserId (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<NotificationResponse>>builder()
                .data(notificationService.findAllByUserId( page, size)).build();
    }

    @PutMapping("/{notificationId}")
    public ApiResponse<NotificationResponse> alreadyRead (@PathVariable("notificationId") String notificationId){
        return ApiResponse.<NotificationResponse>builder()
                .data(notificationService.alreadyRead(notificationId)).build();
    }
}
