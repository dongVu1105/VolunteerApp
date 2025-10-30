package com.dongVu1105.identity_service.mapper;

import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserProfileResponse;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest (UserCreationRequest request);

}
