package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.constant.PredefinedRole;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.mapper.UserMapper;
import com.dongVu1105.identity_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse create (UserCreationRequest request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatusAccount(true);
        // update profile

        //
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return UserResponse.builder()
                .id(user.getId())
                .role(user.getRole())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }


}
