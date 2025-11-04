package com.dongVu1105.notification_service.controller;



import com.dongVu1105.notification_service.constant.PredefinedNotification;
import com.dongVu1105.notification_service.dto.request.EventNoti;
import com.dongVu1105.notification_service.dto.request.EventResponse;
import com.dongVu1105.notification_service.dto.request.EventUserNoti;
import com.dongVu1105.notification_service.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListenNotificationController {
    NotificationService notificationService;

    @KafkaListener(topics = "new-event")
    public void listenNewEventNotification(EventNoti eventNoti){
        notificationService.sendAdmin(eventNoti, PredefinedNotification.NEW_EVENT);
    }

    @KafkaListener(topics = "accept-event")
    public void listenAcceptEventNotification(EventNoti eventNoti){
        notificationService.sendEventManager(eventNoti, PredefinedNotification.ACCEPT_EVENT);
    }

    @KafkaListener(topics = "reject-event")
    public void listenRejectEventNotification(EventResponse eventResponse){
        notificationService.sendEventManagerForReject(eventResponse, PredefinedNotification.REJECT_EVENT);
    }

    @KafkaListener(topics = "register-event")
    public void listenRegisterEventNotification(EventUserNoti eventUserNoti){

        notificationService.sendEventManagerForRegister(eventUserNoti, PredefinedNotification.NEW_REGISTER);
    }

    @KafkaListener(topics = "accept-register")
    public void listenRegisterAcceptNotification(EventNoti eventNoti){
        notificationService.sendUser(eventNoti, PredefinedNotification.ACCEPT_REGISTER);
    }

    @KafkaListener(topics = "reject-register")
    public void listenRegisterRejectNotification(EventNoti eventNoti){
        notificationService.sendUser(eventNoti, PredefinedNotification.REJECT_REGISTER);
    }

    @KafkaListener(topics = "confirm-completion")
    public void listenConfirmCompletionNotification(EventNoti eventNoti){
        notificationService.sendUser(eventNoti, PredefinedNotification.CONFIRM_COMPLETION);
    }

}
