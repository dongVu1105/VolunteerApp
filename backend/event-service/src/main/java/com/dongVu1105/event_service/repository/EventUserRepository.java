package com.dongVu1105.event_service.repository;

import com.dongVu1105.event_service.entity.Event;
import com.dongVu1105.event_service.entity.EventUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventUserRepository extends MongoRepository<EventUser, String> {
    EventUser findByEventUserHash (String eventUserHash);
    Page<EventUser> findAllByStatusAndUserId (String status, String userId, Pageable pageable);
    Page<EventUser> findAllByEventId (String eventId, Pageable pageable);
    Page<EventUser> findAllByStatusAndEventId (String status, String eventId, Pageable pageable);
    List<EventUser> findAllByEventId (String eventId);
}
