package com.dongVu1105.event_service.mapper;

import com.dongVu1105.event_service.dto.request.EventUserCreationRequest;
import com.dongVu1105.event_service.dto.response.EventUserResponse;
import com.dongVu1105.event_service.entity.EventUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventUserMapper {
    EventUser toEventUser (EventUserCreationRequest request);
    EventUserResponse toEventUserResponse (EventUser eventUser);
}
