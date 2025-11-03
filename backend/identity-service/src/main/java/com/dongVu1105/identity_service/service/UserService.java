package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.constant.PredefinedRole;
import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserProfileResponse;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.entity.Role;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.mapper.ProfileMapper;
import com.dongVu1105.identity_service.mapper.UserMapper;
import com.dongVu1105.identity_service.repository.RoleRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import com.dongVu1105.identity_service.repository.httpClient.UserProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserProfileClient userProfileClient;
    ProfileMapper profileMapper;

    public UserResponse create (UserCreationRequest request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatusAccount(true);
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRole()));
        user.setRole(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        ProfileCreationRequest profileCreationRequest = profileMapper.toProfileCreationRequest(request);
        profileCreationRequest.setUserId(user.getId());
        UserProfileResponse userProfileResponse = userProfileClient.create(profileCreationRequest).getData();
        return UserResponse.builder()
                .id(user.getId())
                .role(user.getRole())
                .email(user.getEmail())
                .password(user.getPassword())
                .birthday(userProfileResponse.getBirthday())
                .firstName(userProfileResponse.getFirstName())
                .lastName(userProfileResponse.getLastName())
                .gender(userProfileResponse.isGender())
                .phoneNumber(userProfileResponse.getPhoneNumber())
                .username(userProfileResponse.getUsername())
                .identityNumber(userProfileResponse.getIdentityNumber())
                .build();
    }

    public UserResponse findById (String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }


}
