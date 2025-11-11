package com.dongVu1105.event_service.repository;

import com.dongVu1105.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    Page<Event> findAllByStatusEventAndCategoryAndStartDateGreaterThanEqualAndFinishDateLessThanEqual
            (boolean statusEvent,
             String category,
             Instant startDateFrom,
             Instant finishDateTo,
             Pageable pageable);
    Page<Event> findAllByStatusEvent (boolean statusEvent, Pageable pageable);
    Page<Event> findAllByIdIn (List<String> eventIdList, Pageable pageable);
}
