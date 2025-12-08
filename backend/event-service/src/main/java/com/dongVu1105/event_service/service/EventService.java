package com.dongVu1105.event_service.service;

import com.dongVu1105.event_service.dto.request.EventCreationRequest;
import com.dongVu1105.event_service.dto.request.EventNoti;
import com.dongVu1105.event_service.dto.request.EventUpdationRequest;
import com.dongVu1105.event_service.dto.response.*;
import com.dongVu1105.event_service.entity.Event;
import com.dongVu1105.event_service.exception.AppException;
import com.dongVu1105.event_service.exception.ErrorCode;
import com.dongVu1105.event_service.mapper.EventMapper;
import com.dongVu1105.event_service.repository.EventRepository;
import com.dongVu1105.event_service.repository.httpclient.FileClient;
import com.dongVu1105.event_service.repository.httpclient.IdentityClient;
import com.dongVu1105.event_service.repository.httpclient.UserProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    @NonFinal
    @Value("${app.account.admin}")
    String ADMIN_EMAIL;

    EventRepository eventRepository;
    EventMapper eventMapper;
    DateTimeFormatter dateTimeFormatter;
    IdentityClient identityClient;
    FileClient fileClient;
    KafkaTemplate<String, Object> kafkaTemplate;
    UserProfileClient userProfileClient;

    @PreAuthorize("hasRole('EVENT_MANAGER')")
    @CachePut(value = "events", key = "#result.id")
    public EventResponse create (EventCreationRequest request, MultipartFile file){
        Event event = eventMapper.toEvent(request);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        event.setManagerId(userId);
        event.setCreatedDate(Instant.now());
        FileResponse fileResponse = fileClient.uploadMedia(file).getData();
        event.setImage(fileResponse.getUrl());
        event.setStatusEvent(false);
        event = eventRepository.save(event);
        UserResponse admin = identityClient.findByEmail(ADMIN_EMAIL).getData();
        kafkaTemplate.send("new-event",
                EventNoti.builder().eventId(event.getId()).eventTitle(event.getTitle()).receiverId(admin.getId()).build());
        return this.toEventResponse(event);
    }

    @PreAuthorize("hasRole('EVENT_MANAGER')")
    @CachePut(value = "events", key = "#request.id")
    public EventResponse update (EventUpdationRequest request, MultipartFile file){
        Event event = eventRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        eventMapper.update(request, event);
        if(!Objects.isNull(file)){
            FileResponse fileResponse = fileClient.uploadMedia(file).getData();
            event.setImage(fileResponse.getUrl());
        }
        event = eventRepository.save(event);
        return this.toEventResponse(event);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @CachePut(value = "events", key = "#eventId")
    public EventResponse acceptEvent (String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        event.setStatusEvent(true);
        event = eventRepository.save(event);
        kafkaTemplate.send("accept-event",
                EventNoti.builder().eventId(event.getId()).eventTitle(event.getTitle()).receiverId(event.getManagerId()).build());
        return this.toEventResponse(event);
    }

    @PreAuthorize("hasAnyRole('EVENT_MANAGER', 'ADMIN')")
    @CacheEvict(value = "events", key = "#eventId")
    public void delete (String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse admin = identityClient.findById(userId).getData();
        if (Objects.isNull(admin)){
            throw new AppException(ErrorCode.UNCONNECTED_SERVICE);
        }
        if(!(userId.equals(event.getManagerId()) || admin.getEmail().equals(ADMIN_EMAIL))){
            throw new AppException(ErrorCode.CANNOT_DELETE_EVENT);
        }
        if (admin.getEmail().equals(ADMIN_EMAIL)){
            kafkaTemplate.send("reject-event", eventMapper.toEventResponse(event));
        }
        eventRepository.deleteById(eventId);
    }

    @Cacheable(value = "events", key = "#id")
    public EventResponse findById (String id){
        return this.toEventResponse(eventRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.EVENT_NOT_EXISTED) ));
    }

    @Cacheable(value = "event-status", key = "#eventId")
    public Boolean ableToPost (String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        return event.isStatusEvent();
    }

    @PreAuthorize("hasRole('USER')")
    // @Cacheable(value = "events-by-category-date", key = "{#page, #size, #category, #fromDate, #toDate}")
    public PageResponse<EventResponse> findAllByCategoryAndDate (int page, int size, String category, Instant fromDate, Instant toDate){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Event> eventPage = eventRepository.findAllByStatusEventAndCategoryAndStartDateGreaterThanEqualAndFinishDateLessThanEqual
                (true,category,fromDate, toDate, pageable);
        var eventData = eventPage.getContent().stream().map(this::toEventResponse).toList();

        return PageResponse.<EventResponse>builder()
                .currentPage(page)
                .pageSize(eventPage.getSize())
                .totalPages(eventPage.getTotalPages())
                .totalElements(eventPage.getTotalElements())
                .hasNextPage(eventPage.hasNext())
                .hasPreviousPage(eventPage.hasPrevious())
                .result(eventData)
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    // @Cacheable(value = "pending-events", key = "{#page, #size}")
    public PageResponse<EventResponse> findAllPendingEvent (int page, int size){
        Sort sort = Sort.by("createdDate").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Event> eventPage = eventRepository.findAllByStatusEvent(false, pageable);
        var eventData = eventPage.getContent().stream().map(this::toEventResponse).toList();

        return PageResponse.<EventResponse>builder()
                .currentPage(page)
                .pageSize(eventPage.getSize())
                .totalPages(eventPage.getTotalPages())
                .totalElements(eventPage.getTotalElements())
                .hasNextPage(eventPage.hasNext())
                .hasPreviousPage(eventPage.hasPrevious())
                .result(eventData)
                .build();
    }

    public List<EventResponse> findAll (){
        return eventRepository.findAll().stream().map(eventMapper::toEventResponse).toList();
    }


    private EventResponse toEventResponse (Event event){
        EventResponse eventResponse = eventMapper.toEventResponse(event);
        String formatCreatedDate = dateTimeFormatter.format(event.getCreatedDate());
        eventResponse.setCreatedDate(formatCreatedDate);
        eventResponse.setManagerUsername(
                userProfileClient.findById(event.getManagerId()).getData().getUsername());
        return eventResponse;
    }
}
