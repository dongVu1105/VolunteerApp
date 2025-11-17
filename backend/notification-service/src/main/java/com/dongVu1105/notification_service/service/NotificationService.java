package com.dongVu1105.notification_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.notification_service.constant.PredefinedNotification;
import com.dongVu1105.notification_service.dto.request.*;
import com.dongVu1105.notification_service.dto.response.EventInfo;
import com.dongVu1105.notification_service.dto.response.EventUserInfo;
import com.dongVu1105.notification_service.dto.response.NotificationResponse;
import com.dongVu1105.notification_service.dto.response.PageResponse;
import com.dongVu1105.notification_service.entity.Notification;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.exception.AppException;
import com.dongVu1105.notification_service.exception.ErrorCode;
import com.dongVu1105.notification_service.mapper.NotificationMapper;
import com.dongVu1105.notification_service.repository.NotificationRepository;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

@Slf4j
@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    SocketIOServer socketIOServer;
    WebSocketSessionRepository webSocketSessionRepository;
    ObjectMapper objectMapper;
    NotificationMapper notificationMapper;
    DateTimeFormatter dateTimeFormatter;

    public void sendUser (EventNoti eventNoti, PredefinedNotification predefinedNotification){
        Notification<EventInfo> notification = Notification.<EventInfo>builder()
                .subject(predefinedNotification.getSubject())
                .message(predefinedNotification.getContent() + eventNoti.getEventTitle())
                .info(EventInfo.builder().eventId(eventNoti.getEventId()).build())
                .receiverId(List.of(eventNoti.getReceiverId()))
                .createdDate(Instant.now())
                .isRead(false)
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(
                notification.getReceiverId().stream().findFirst().orElseThrow(
                        () -> new AppException(ErrorCode.RECEIVER_NOT_FOUND)));
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(finalNotification);
                        socketIOClient.sendEvent("user-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void sendEventManager (EventNoti eventNoti, PredefinedNotification predefinedNotification){
        Notification<EventInfo> notification = Notification.<EventInfo>builder()
                .subject(predefinedNotification.getSubject())
                .message(predefinedNotification.getContent() + eventNoti.getEventTitle())
                .info(EventInfo.builder().eventId(eventNoti.getEventId()).build())
                .receiverId(List.of(eventNoti.getReceiverId()))
                .isRead(false)
                .createdDate(Instant.now())
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(
                notification.getReceiverId().stream().findFirst().orElseThrow(
                        () -> new AppException(ErrorCode.RECEIVER_NOT_FOUND)));
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(finalNotification);
                        socketIOClient.sendEvent("event-manager-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void sendAdmin (EventNoti eventNoti, PredefinedNotification predefinedNotification){
        Notification<EventInfo> notification = Notification.<EventInfo>builder()
                .subject(predefinedNotification.getSubject())
                .message(predefinedNotification.getContent() + eventNoti.getEventTitle())
                .info(EventInfo.builder().eventId(eventNoti.getEventId()).build())
                .receiverId(List.of(eventNoti.getReceiverId()))
                .createdDate(Instant.now())
                .isRead(false)
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(
                notification.getReceiverId().stream().findFirst().orElseThrow(
                        () -> new AppException(ErrorCode.RECEIVER_NOT_FOUND)));
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(finalNotification);
                        socketIOClient.sendEvent("admin-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void sendEventManagerForRegister(EventUserNoti eventUserNoti, PredefinedNotification predefinedNotification) {
        Notification<EventUserInfo> notification = Notification.<EventUserInfo>builder()
                .subject(predefinedNotification.getSubject())
                .message(eventUserNoti.getUsername() + predefinedNotification.getContent())
                .info(EventUserInfo.builder().eventUserId(eventUserNoti.getEventUserId()).build())
                .receiverId(List.of(eventUserNoti.getReceiverId()))
                .createdDate(Instant.now())
                .isRead(false)
                .build();
        Notification<EventUserInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(
                notification.getReceiverId().stream().findFirst().orElseThrow(
                        () -> new AppException(ErrorCode.RECEIVER_NOT_FOUND)));
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(finalNotification);
                        socketIOClient.sendEvent("event-manager-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }


    public void sendEventManagerForReject(EventResponse eventResponse, PredefinedNotification predefinedNotification) {
        Notification<EventResponse> notification = Notification.<EventResponse>builder()
                .subject(predefinedNotification.getSubject())
                .message(predefinedNotification.getContent() + eventResponse.getTitle())
                .info(eventResponse)
                .receiverId(List.of(eventResponse.getManagerId()))
                .createdDate(Instant.now())
                .isRead(false)
                .build();
        Notification<EventResponse> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(
                notification.getReceiverId().stream().findFirst().orElseThrow(
                        () -> new AppException(ErrorCode.RECEIVER_NOT_FOUND)));
        if(Objects.nonNull(webSocketSession)){
            socketIOServer.getAllClients().forEach(socketIOClient -> {
                if(socketIOClient.getSessionId().toString().equals(webSocketSession.getSocketSessionId())){
                    String noti = null;
                    try {
                        noti = objectMapper.writeValueAsString(finalNotification);
                        socketIOClient.sendEvent("event-manager-noti", noti);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void postNotification (PostNoti postNoti, PredefinedNotification predefinedNotification){
        Notification<PostNoti> notification = Notification.<PostNoti>builder()
                .subject(predefinedNotification.getSubject())
                .message(postNoti.getCreatorName() +predefinedNotification.getContent())
                .info(postNoti)
                .receiverId(postNoti.getReceiverId())
                .createdDate(Instant.now())
                .isRead(false)
                .build();
        Notification<PostNoti> finalNotification = notificationRepository.save(notification);
        System.out.println("finalNotification.getReceiverId(): "+ finalNotification.getReceiverId());
        // Test
            List<String> receiverIds =
                    notification.getReceiverId().stream().filter(receiver -> !receiver.equals(postNoti.getCreatorName())).toList();
            System.out.println("receiverIds " +receiverIds);
            System.out.println("postNoti.getCreatorId(): "+postNoti.getCreatorName());
        //
        List<WebSocketSession> webSocketSessionList = webSocketSessionRepository.findAllByUserIdIn(
                notification.getReceiverId().stream().filter(receiver -> !receiver.equals(postNoti.getCreatorId())).toList());
        System.out.println("webSocketSessionList"+webSocketSessionList.toString());
        Map<String, WebSocketSession> lookup = new TreeMap<>();
        for (WebSocketSession webSocketSession : webSocketSessionList){
            lookup.put(webSocketSession.getSocketSessionId(), webSocketSession);
        }
        socketIOServer.getAllClients().forEach(socketIOClient -> {
            WebSocketSession webSocketSession = lookup.get(socketIOClient.getSessionId().toString());
            if(Objects.nonNull(webSocketSession)){
                System.out.println("sessionId: "+webSocketSession.getId());
                String message = null;
                try {
                    message = objectMapper.writeValueAsString(finalNotification);
                    socketIOClient.sendEvent("post", message);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public PageResponse<NotificationResponse> findAllByUserId ( int page, int size){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Notification> notificationPage = notificationRepository.findAllByReceiverIdContains(userId, pageable);
        var notiData = notificationPage.getContent().stream().map(this::toNotificationResponse).toList();
        return PageResponse.<NotificationResponse>builder()
                .currentPage(page)
                .pageSize(notificationPage.getSize())
                .totalElements(notificationPage.getTotalElements())
                .totalPages(notificationPage.getTotalPages())
                .hasPreviousPage(notificationPage.hasPrevious())
                .hasNextPage(notificationPage.hasNext())
                .result(notiData).build();
    }

    public NotificationResponse alreadyRead (String notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.setRead(true);
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    private NotificationResponse toNotificationResponse (Notification notification){
        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
        notificationResponse.setCreatedDate(dateTimeFormatter.format(notification.getCreatedDate()));
        return notificationResponse;
    }
}
