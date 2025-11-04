package com.dongVu1105.notification_service.controller;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.dongVu1105.notification_service.dto.request.IntrospectRequest;
import com.dongVu1105.notification_service.dto.response.IntrospectResponse;
import com.dongVu1105.notification_service.entity.WebSocketSession;
import com.dongVu1105.notification_service.service.IdentityService;
import com.dongVu1105.notification_service.service.WebSocketSessionService;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SocketHandler {
    SocketIOServer server;
    IdentityService identityService;
    WebSocketSessionService webSocketSessionService;

    @OnConnect
    public void clientConnected (SocketIOClient client) throws ParseException {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        IntrospectResponse introspectResponse = identityService.introspect(
                IntrospectRequest.builder().accessToken(token).build());
        if(introspectResponse.isValid()){
            log.info("Client connected: {}", client.getSessionId());
            String userId = SignedJWT.parse(token).getJWTClaimsSet().getSubject();
            webSocketSessionService.create(WebSocketSession.builder()
                            .userId(userId)
                            .socketSessionId(client.getSessionId().toString())
                            .createdDate(Instant.now())
                            .build());

        } else {
            log.error("Authentication fail: {}", client.getSessionId());
            client.disconnect();
        }
    }

    @OnDisconnect
    public void clientDisconnected (SocketIOClient client){
        log.info("Client disConnected: {}", client.getSessionId());
        webSocketSessionService.delete(client.getSessionId().toString());
    }

    @PostConstruct
    public void startServer (){
        server.start();
        server.addListeners(this);
        log.info("Socket server started");
    }

    @PreDestroy
    public void stopServer (){
        server.stop();
        log.info("Socket server stopped");
    }
}
