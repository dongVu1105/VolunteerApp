package com.dongVu1105.notification_service.service;



import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.repository.WebSocketSessionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WebSocketSessionService {
    WebSocketSessionRepository webSocketSessionRepository;

    public WebSocketSession create (WebSocketSession webSocketSession){
        return webSocketSessionRepository.save(webSocketSession);
    }

    public void delete (String socketSessionId){
        webSocketSessionRepository.deleteBySocketSessionId(socketSessionId);
    }
}
