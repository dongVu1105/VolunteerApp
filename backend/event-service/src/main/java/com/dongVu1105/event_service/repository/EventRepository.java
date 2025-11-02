package com.dongVu1105.event_service.repository;

import com.dongVu1105.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Page<Event> findAllByStatusEventAndCategory (boolean statusEvent, String category, Pageable pageable);
    Page<Event> findAllByStatusEvent (boolean statusEvent, Pageable pageable);
}
