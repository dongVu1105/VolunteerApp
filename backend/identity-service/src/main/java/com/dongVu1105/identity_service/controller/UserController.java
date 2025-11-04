package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register (@RequestBody UserCreationRequest request){
        return ApiResponse.<UserResponse>builder().data(userService.create(request)).build();
    }

    @GetMapping("/find-by-id/{id}")
    public ApiResponse<UserResponse> findById (@PathVariable("id") String id){
        return ApiResponse.<UserResponse>builder().data(userService.findById(id)).build();
    }

    @GetMapping("/find-by-email/{email}")
    public ApiResponse<UserResponse> findByEmail (@PathVariable("email") String email){
        return ApiResponse.<UserResponse>builder().data(userService.findByEmail(email)).build();
    }
}
