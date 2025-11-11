package com.dongVu1105.profile_service.service;

import com.dongVu1105.profile_service.dto.request.GetProfileRequest;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.response.PageResponse;
import com.dongVu1105.profile_service.dto.response.UserProfileResponse;
import com.dongVu1105.profile_service.entity.UserProfile;
import com.dongVu1105.profile_service.mapper.UserProfileMapper;
import com.dongVu1105.profile_service.repository.UserProfileRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileService {

    @NonFinal
    @Value("${app.services.file.default-avatar}")
    private String DEFAULT_AVATAR;

    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;

    public UserProfileResponse create (ProfileCreationRequest request){
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile.setAvatar(DEFAULT_AVATAR);
        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(userProfile));
    }

    public UserProfileResponse findByUserId (String userId){
        return userProfileMapper.toUserProfileResponse(userProfileRepository.findByUserId(userId));
    }

    public PageResponse<UserProfileResponse> findAllByUserIdList (GetProfileRequest request){
        Sort sort = Sort.by("username");
        Pageable pageable = PageRequest.of(request.getPage()- 1, request.getSize(), sort);
        Page<UserProfile> userProfilePage = userProfileRepository.findAllByUserIdIn(request.getUserIdList(), pageable);
        var userProfileData = userProfilePage.getContent().stream().map(userProfileMapper::toUserProfileResponse).toList();
        return PageResponse.<UserProfileResponse>builder()
                .currentPage(request.getPage())
                .pageSize(userProfilePage.getSize())
                .totalElements(userProfilePage.getTotalElements())
                .totalPages(userProfilePage.getTotalPages())
                .result(userProfileData).build();
    }
}
