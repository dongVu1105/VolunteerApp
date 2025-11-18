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

import java.util.List;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalProfileController {
    UserProfileService userProfileService;

    @PostMapping("/user")
    public ApiResponse<UserProfileResponse> create (@RequestBody ProfileCreationRequest request){
        System.out.println("Da vao profile controller");
        return ApiResponse.<UserProfileResponse>builder()
                .data(userProfileService.create(request)).build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<UserProfileResponse> findById (@PathVariable("userId") String userId){
        System.out.println("Da vao internal profile controller");
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.findByUserId(userId)).build();
    }

    @PostMapping("/userId-list")
    public ApiResponse<List<UserProfileResponse>> findAllByUserIdList (@RequestBody GetProfileRequest request){
        return ApiResponse.<List<UserProfileResponse>>builder()
                .data(userProfileService.findAllByUserIdList(request)).build();
    }

    @GetMapping("/find/{username}")
    public ApiResponse<UserProfileResponse> findByUsername (@PathVariable("username") String username){
        return ApiResponse.<UserProfileResponse>builder().data(userProfileService.findByUsername(username)).build();
    }
}
