package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.response.UserProfileResponse;
import com.dongVu1105.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProfileController {
    UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> findById (@PathVariable("userId") String userId){
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.findByUserId(userId)).build();
    }

    @GetMapping("/find/{username}")
    public ApiResponse<UserProfileResponse> findByUsername (@PathVariable("username") String username){
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.findByUsername(username)).build();
    }
}
