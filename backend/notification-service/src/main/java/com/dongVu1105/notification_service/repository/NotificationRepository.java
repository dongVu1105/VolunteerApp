package com.dongVu1105.notification_service.repository;

import com.dongVu1105.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    Page<Notification> findAllByReceiverIdContains (String userId, Pageable pageable);
}
