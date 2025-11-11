package com.dongVu1105.event_service.controller;

import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.request.EventCreationRequest;
import com.dongVu1105.event_service.dto.request.EventUpdationRequest;
import com.dongVu1105.event_service.dto.response.EventResponse;
import com.dongVu1105.event_service.dto.response.PageResponse;
import com.dongVu1105.event_service.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    EventService eventService;
    ObjectMapper objectMapper;

    // Quản lí sự kiện tạo event
    @PostMapping("/create")
    public ApiResponse<EventResponse> create (
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false)MultipartFile file) throws JsonProcessingException {
        EventCreationRequest request = objectMapper.readValue(requestJson, EventCreationRequest.class);
        return ApiResponse.<EventResponse>builder().data(eventService.create(request, file)).build();
    }

    // Quản lí sự kiện sửa event
    @PutMapping("/update")
    public ApiResponse<EventResponse> update (
            @RequestPart(value = "request") String requestJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        EventUpdationRequest request = objectMapper.readValue(requestJson, EventUpdationRequest.class);
        return ApiResponse.<EventResponse>builder().data(eventService.update(request, file)).build();
    }

    // Người dùng lọc event theo danh mục
    @GetMapping("/category")
    public ApiResponse<PageResponse<EventResponse>> findAllByCategory (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "category", required = false, defaultValue = "Từ thiện") String category){
        System.out.println(category);
        return ApiResponse.<PageResponse<EventResponse>>builder()
                .data(eventService.findAllByCategory(page, size, category)).build();
    }

    // Tìm event theo id
    @GetMapping("/{eventId}")
    public ApiResponse<EventResponse> findById (@PathVariable("eventId") String eventId){
        return ApiResponse.<EventResponse>builder().data(eventService.findById(eventId)).build();
    }

    @GetMapping("/status/{eventId}")
    public ApiResponse<Boolean> ableToPost (@PathVariable("eventId") String eventId){
        return ApiResponse.<Boolean>builder().data(eventService.ableToPost(eventId)).build();
    }

    // Admin tìm các sự kiện chưa được chấp nhận
    @GetMapping("/pending")
    public ApiResponse<PageResponse<EventResponse>> findAllPendingEvent (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<EventResponse>>builder()
                .data(eventService.findAllPendingEvent(page, size)).build();
    }

    // Admin chấp nhận event
    @PutMapping("/accept/{eventId}")
    public ApiResponse<EventResponse> acceptEvent (@PathVariable("eventId") String eventId){
        return ApiResponse.<EventResponse>builder().data(eventService.acceptEvent(eventId)).build();
    }

    // Admin xóa event
    @DeleteMapping("/delete/{eventId}")
    public ApiResponse<Void> delete (@PathVariable("eventId") String eventId){
        eventService.delete(eventId);
        return ApiResponse.<Void>builder().build();
    }


}
