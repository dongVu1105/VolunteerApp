package com.dongVu1105.profile_service.controller;

import com.dongVu1105.profile_service.dto.ApiResponse;
import com.dongVu1105.profile_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.profile_service.dto.response.UserProfileResponse;
import com.dongVu1105.profile_service.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
