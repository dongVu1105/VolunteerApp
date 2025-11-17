package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.constant.PredefinedRole;
import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.*;
import com.dongVu1105.identity_service.entity.Role;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.mapper.ProfileMapper;
import com.dongVu1105.identity_service.mapper.RoleMapper;
import com.dongVu1105.identity_service.mapper.UserMapper;
import com.dongVu1105.identity_service.repository.RoleRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import com.dongVu1105.identity_service.repository.httpClient.UserProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    RoleMapper roleMapper;

    public UserResponse create (UserCreationRequest request){
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatusAccount(true);
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRole()));
        user.setRole(roles);
        if(Objects.nonNull(userProfileClient.findByUsername(request.getUsername()).getData())){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        ProfileCreationRequest profileCreationRequest = profileMapper.toProfileCreationRequest(request);
        profileCreationRequest.setUserId(user.getId());
        UserProfileResponse userProfileResponse = null;
        try {
            userProfileResponse = userProfileClient.create(profileCreationRequest).getData();
        } catch (Exception e) {
            userRepository.deleteById(user.getId());
            throw new AppException(ErrorCode.FAIL_REGISTRATION);
        }
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
                .avatar(userProfileResponse.getAvatar())
                .build();
    }

    public UserResponse findById (String id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public UserResponse findByEmail (String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    public List<RoleResponse> findAll (){
        return roleRepository.findAll().stream()
                .filter(role -> !role.getName().equals(PredefinedRole.ADMIN_ROLE)).map(roleMapper::toRoleResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse lockAccount (String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatusAccount(false);
        user = userRepository.save(user);
        return userMapper.toAccountResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AccountResponse unLockAccount (String userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatusAccount(true);
        user = userRepository.save(user);
        return userMapper.toAccountResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AccountResponse> findAllAccount (int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAll(pageable);
        var userData = userPage.getContent().stream().map(userMapper::toAccountResponse).toList();

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .hasPreviousPage(userPage.hasPrevious())
                .hasNextPage(userPage.hasNext())
                .result(userData)
                .build();
    }

    public PageResponse<AccountResponse> findAccountByKeyword (int page, int size, String keyword){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> userPage = userRepository.findAllByEmailContaining(keyword, pageable);
        var userData = userPage.getContent().stream().map(userMapper::toAccountResponse).toList();

        return PageResponse.<AccountResponse>builder()
                .currentPage(page)
                .pageSize(userPage.getSize())
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .hasPreviousPage(userPage.hasPrevious())
                .hasNextPage(userPage.hasNext())
                .result(userData)
                .build();
    }


}
