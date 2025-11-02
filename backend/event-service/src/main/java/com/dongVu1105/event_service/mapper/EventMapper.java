package com.dongVu1105.event_service.mapper;

import com.dongVu1105.event_service.dto.request.EventCreationRequest;
import com.dongVu1105.event_service.dto.request.EventUpdationRequest;
import com.dongVu1105.event_service.dto.response.EventResponse;
import com.dongVu1105.event_service.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "createdDate", ignore = true)
    EventResponse toEventResponse (Event event);
    Event toEvent (EventCreationRequest request);
    void update (EventUpdationRequest request, @MappingTarget Event event);
}
