package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.GetProfileRequest;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.response.PageResponse;
import com.dongVu1105.profile_service.dto.response.UserProfileResponse;
import com.dongVu1105.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    UserProfileService userProfileService;

    @PostMapping("/user")
    public ApiResponse<UserProfileResponse> create (@RequestBody ProfileCreationRequest request){
        return ApiResponse.<UserProfileResponse>builder()
                .data(userProfileService.create(request)).build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> findById (@PathVariable("userId") String userId){
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.findByUserId(userId)).build();
    }

    @GetMapping("/userId-list")
    public ApiResponse<PageResponse<UserProfileResponse>> findAllByUserIdList (@RequestBody GetProfileRequest request){
        return ApiResponse.<PageResponse<UserProfileResponse>>builder()
                .data(userProfileService.findAllByUserIdList(request)).build();
    }
}
