package com.dongVu1105.event_service.controller;

import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.request.EventUserCreationRequest;
import com.dongVu1105.event_service.dto.response.EventResponse;
import com.dongVu1105.event_service.dto.response.EventUserResponse;
import com.dongVu1105.event_service.dto.response.PageResponse;
import com.dongVu1105.event_service.dto.response.UserProfileResponse;
import com.dongVu1105.event_service.service.EventUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventUserController {
    EventUserService eventUserService;

    // Người dùng đăng kí tham gia sự kiện
    @PostMapping("/register")
    public ApiResponse<EventUserResponse> eventRegistration (@RequestBody EventUserCreationRequest request){
        return ApiResponse.<EventUserResponse>builder()
                .data(eventUserService.eventRegistration(request)).build();
    }

    // Người dùng thoái đăng sự kiện
    @DeleteMapping("/unsubscribe/{eventId}")
    public ApiResponse<Void> unsubscribeEvent (@PathVariable("eventId") String eventId){
        eventUserService.unsubscribeEvent(eventId);
        return ApiResponse.<Void>builder().build();
    }

    // Người dùng xem các sự kiện được đánh dấu đã hoàn thành
    @GetMapping("/completed-event")
    public ApiResponse<PageResponse<EventResponse>> findAllMyCompletedEvent (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<EventResponse>>builder()
                .data(eventUserService.findAllMyCompletedEventByUserId(page, size)).build();
    }

    // Quản lí sự kiện xem các user chưa được xác nhận tham gia
    @GetMapping("/pending")
    public ApiResponse<PageResponse<UserProfileResponse>> findAllPendingUser (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "eventId") String eventId){
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .data(eventUserService.findAllPendingUser(page, size, eventId)).build();
    }

    // Quản lí sự kiện xác nhận đăng kí của người dùng
    @PutMapping("/accept-registration/{eventUserId}")
    public ApiResponse<EventUserResponse> acceptUserRegistration (
            @PathVariable("eventUserId") String eventUserId){
        return ApiResponse.<EventUserResponse>builder()
                .data(eventUserService.acceptUserRegistration(eventUserId)).build();
    }

    // Quản lí sự kiện từ chối đăng kí của người dùng
    @DeleteMapping("/reject-registration/{eventUserId}")
    public ApiResponse<Void> rejectRegistration (@PathVariable("eventUserId") String eventUserId){
        eventUserService.deleteUserRegistration(eventUserId);
        return ApiResponse.<Void>builder().build();
    }

    // Quản lí sự kiện tìm id của eventUser
    @GetMapping("/{eventUserId}")
    public ApiResponse<EventUserResponse> findById (@PathVariable("eventUserId") String eventUserId){
        return ApiResponse.<EventUserResponse>builder().data(eventUserService.findById(eventUserId)).build();
    }

    // Quản lí sự kiện xác nhận hoàn thành cho người tham gia
    @PutMapping("/confirm-completion/{eventUserId}")
    public ApiResponse<EventUserResponse> confirmUserCompletion (
            @PathVariable("eventUserId") String eventUserId){
        return ApiResponse.<EventUserResponse>builder()
                .data(eventUserService.confirmUserCompletion(eventUserId)).build();
    }

    // Quản lí sự kiện xem các user đang tham gia event
    @GetMapping("/attending")
    public ApiResponse<PageResponse<UserProfileResponse>> findAllAttendingUser (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "eventId") String eventId){
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .data(eventUserService.findAllAttendingUser(page, size, eventId)).build();
    }

    // Quản lí sự kiện xem danh sách user trong event
    @GetMapping("/find-all")
    public ApiResponse<PageResponse<UserProfileResponse>> findAllUserInEvent (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "eventId") String eventId){
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .data(eventUserService.findAllUserInEvent(page, size, eventId)).build();
    }

    // Kiem tra user co trong event hay khong
    @GetMapping("/exist/{userId}/{eventId}")
    public ApiResponse<Boolean> isUserInEvent (
            @PathVariable("userId") String userId,
            @PathVariable("eventId") String eventId){
        return ApiResponse.<Boolean>builder().data(eventUserService.isInEvent(userId, eventId)).build();
    }

}
