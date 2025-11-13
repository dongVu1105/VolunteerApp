package com.dongVu1105.event_service.mapper;

import com.dongVu1105.event_service.dto.request.EventUserCreationRequest;
import com.dongVu1105.event_service.dto.response.EventUserInfoResponse;
import com.dongVu1105.event_service.dto.response.EventUserResponse;
import com.dongVu1105.event_service.dto.response.UserProfileResponse;
import com.dongVu1105.event_service.entity.EventUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventUserMapper {
    EventUser toEventUser (EventUserCreationRequest request);
    EventUserResponse toEventUserResponse (EventUser eventUser);
    EventUserInfoResponse toEventUserInfoResponse (EventUser eventUser);
    EventUserInfoResponse toEventUserInfoResponse (UserProfileResponse userProfileResponse);
}
