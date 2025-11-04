package com.dongVu1105.notification_service.repository;



import com.dongVu1105.notification_service.entity.WebSocketSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebSocketSessionRepository extends MongoRepository<WebSocketSession, String> {
    void deleteBySocketSessionId (String socketSessionId);
    List<WebSocketSession> findAllByUserIdIn (List<String> userIds);
    WebSocketSession findByUserId (String userId);
}
