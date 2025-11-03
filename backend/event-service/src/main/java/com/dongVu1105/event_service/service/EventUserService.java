package com.dongVu1105.event_service.service;

import com.dongVu1105.event_service.dto.request.EventUserCreationRequest;
import com.dongVu1105.event_service.dto.response.*;
import com.dongVu1105.event_service.entity.Event;
import com.dongVu1105.event_service.entity.EventUser;
import com.dongVu1105.event_service.enums.EventUserStatus;
import com.dongVu1105.event_service.exception.AppException;
import com.dongVu1105.event_service.exception.ErrorCode;
import com.dongVu1105.event_service.mapper.EventUserMapper;
import com.dongVu1105.event_service.repository.EventRepository;
import com.dongVu1105.event_service.repository.EventUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventUserService {

    EventUserMapper eventUserMapper;
    EventUserRepository eventUserRepository;
    EventRepository eventRepository;

    public EventUserResponse eventRegistration (EventUserCreationRequest request){
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String eventUserHash = this.hashEventUser(userId, request.getEventId());
        EventUser eventUser = eventUserRepository.findByEventUserHash(eventUserHash);
        if(!Objects.isNull(eventUser)){
            throw new AppException(ErrorCode.USER_REGISTERED);
        }
        EventUser newEventUser = eventUserMapper.toEventUser(request);
        newEventUser.setEventUserHash(eventUserHash);
        newEventUser.setUserId(userId);
        newEventUser.setStatus(EventUserStatus.PENDING.name());
        newEventUser = eventUserRepository.save(newEventUser);
        return eventUserMapper.toEventUserResponse(newEventUser);
    }

    public void unsubscribeEvent (String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String eventUserHash = this.hashEventUser(userId, eventId);
        EventUser eventUser = eventUserRepository.findByEventUserHash(eventUserHash);
        if(Objects.isNull(eventUser)){
            throw new AppException(ErrorCode.USER_NOT_REGISTERED);
        }
        eventUserRepository.deleteById(eventUser.getId());
    }

    /// Cần hiện danh sách thông tin event
    public PageResponse<EventUserResponse> findAllMyCompletedEventByUserId (int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<EventUser> eventUserPage = eventUserRepository.findAllByStatusAndUserId(EventUserStatus.COMPLETED.name(), userId, pageable);
        var eventUserData = eventUserPage.getContent().stream().map(eventUserMapper::toEventUserResponse).toList();

        return PageResponse.<EventUserResponse>builder()
                .currentPage(page)
                .pageSize(eventUserPage.getSize())
                .totalPages(eventUserPage.getTotalPages())
                .totalElements(eventUserPage.getTotalElements())
                .result(eventUserData)
                .build();
    }

    /// Cần hiện thông tin user
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public PageResponse<EventUserResponse> findAllPendingUser (int page, int size, String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EventUser> eventUserPage = eventUserRepository.findAllByStatusAndEventId(EventUserStatus.PENDING.name(), eventId, pageable);
        var eventUserData = eventUserPage.getContent().stream().map(eventUserMapper::toEventUserResponse).toList();

        return PageResponse.<EventUserResponse>builder()
                .currentPage(page)
                .pageSize(eventUserPage.getSize())
                .totalPages(eventUserPage.getTotalPages())
                .totalElements(eventUserPage.getTotalElements())
                .result(eventUserData)
                .build();
    }

    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public EventUserResponse acceptUserRegistration (String eventUserId){
        EventUser eventUser = eventUserRepository.findById(eventUserId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_USER_NOT_EXISTED));
        Event event = eventRepository.findById(eventUser.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        eventUser.setStatus(EventUserStatus.ATTENDING.name());
        eventUser = eventUserRepository.save(eventUser);
        return eventUserMapper.toEventUserResponse(eventUser);
    }

    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public void deleteUserRegistration (String eventUserId){
        EventUser eventUser = eventUserRepository.findById(eventUserId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_USER_NOT_EXISTED));
        Event event = eventRepository.findById(eventUser.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        eventUserRepository.deleteById(eventUser.getId());
    }

    /// Cần hiện thông tin user
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public PageResponse<EventUserResponse> findAllAttendingUser (int page, int size, String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EventUser> eventUserPage = eventUserRepository.findAllByStatusAndEventId(EventUserStatus.ATTENDING.name(), eventId, pageable);
        var eventUserData = eventUserPage.getContent().stream().map(eventUserMapper::toEventUserResponse).toList();

        return PageResponse.<EventUserResponse>builder()
                .currentPage(page)
                .pageSize(eventUserPage.getSize())
                .totalPages(eventUserPage.getTotalPages())
                .totalElements(eventUserPage.getTotalElements())
                .result(eventUserData)
                .build();
    }

    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public EventUserResponse confirmUserCompletion (String eventUserId){
        EventUser eventUser = eventUserRepository.findById(eventUserId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_USER_NOT_EXISTED));
        Event event = eventRepository.findById(eventUser.getEventId())
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        eventUser.setStatus(EventUserStatus.COMPLETED.name());
        eventUser = eventUserRepository.save(eventUser);
        return eventUserMapper.toEventUserResponse(eventUser);
    }

    /// Cần hiện thông tin user
    /// Liệu user xem được không?
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public PageResponse<EventUserResponse> findAllUserInEvent (int page, int size, String eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new AppException(ErrorCode.EVENT_NOT_EXISTED));
        if(!event.isStatusEvent()){
            throw new AppException(ErrorCode.UNAVAILABLE_EVENT);
        }
        String managerId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getManagerId().equals(managerId)){
            throw new AppException(ErrorCode.CANNOT_MODIFY_EVENT);
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<EventUser> eventUserPage = eventUserRepository.findAllByEventId(eventId, pageable);
        var eventUserData = eventUserPage.getContent().stream().map(eventUserMapper::toEventUserResponse).toList();

        return PageResponse.<EventUserResponse>builder()
                .currentPage(page)
                .pageSize(eventUserPage.getSize())
                .totalPages(eventUserPage.getTotalPages())
                .totalElements(eventUserPage.getTotalElements())
                .result(eventUserData)
                .build();
    }

    private String hashEventUser (String userId, String eventId){
        List<String> hashList = new ArrayList<>();
        hashList.add(userId);
        hashList.add(eventId);
        hashList = hashList.stream().sorted().toList();
        StringJoiner stringJoiner = new StringJoiner("_");
        hashList.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
