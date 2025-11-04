package com.dongVu1105.notification_service.service;

import com.corundumstudio.socketio.SocketIOServer;
import com.dongVu1105.notification_service.constant.PredefinedNotification;
import com.dongVu1105.notification_service.dto.request.EventNoti;
import com.dongVu1105.notification_service.dto.request.EventUserNoti;
import com.dongVu1105.notification_service.dto.response.EventInfo;
import com.dongVu1105.notification_service.dto.response.EventUserInfo;
import com.dongVu1105.notification_service.entity.Notification;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.repository.NotificationRepository;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    SocketIOServer socketIOServer;
    WebSocketSessionRepository webSocketSessionRepository;
    ObjectMapper objectMapper;

    public void sendUser (EventNoti eventNoti, PredefinedNotification predefinedNotification){
        Notification<EventInfo> notification = Notification.<EventInfo>builder()
                .subject(predefinedNotification.getSubject())
                .message(predefinedNotification.getContent() + eventNoti.getEventTitle())
                .info(EventInfo.builder().eventId(eventNoti.getEventId()).build())
                .receiverId(eventNoti.getReceiverId())
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(eventNoti.getReceiverId());
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
                .receiverId(eventNoti.getReceiverId())
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(eventNoti.getReceiverId());
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
                .receiverId(eventNoti.getReceiverId())
                .build();
        Notification<EventInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(eventNoti.getReceiverId());
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
                .receiverId(eventUserNoti.getReceiverId())
                .build();
        Notification<EventUserInfo> finalNotification = notificationRepository.save(notification);
        WebSocketSession webSocketSession = webSocketSessionRepository.findByUserId(eventUserNoti.getReceiverId());
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
}
