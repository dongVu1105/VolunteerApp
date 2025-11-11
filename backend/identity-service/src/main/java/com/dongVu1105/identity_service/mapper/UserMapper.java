package com.dongVu1105.identity_service.mapper;

import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.AccountResponse;
import com.dongVu1105.identity_service.dto.response.UserProfileResponse;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", ignore = true)
    User toUser (UserCreationRequest request);
    UserResponse toUserResponse (User user);
    AccountResponse toAccountResponse (User user);
}
