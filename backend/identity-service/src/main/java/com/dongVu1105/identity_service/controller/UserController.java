package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.UserCreationRequest;
import com.dongVu1105.identity_service.dto.response.AccountResponse;
import com.dongVu1105.identity_service.dto.response.PageResponse;
import com.dongVu1105.identity_service.dto.response.RoleResponse;
import com.dongVu1105.identity_service.dto.response.UserResponse;
import com.dongVu1105.identity_service.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register (@Valid @RequestBody UserCreationRequest request){
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

    @GetMapping("/role")
    public ApiResponse<List<RoleResponse>> findAll (){
        return ApiResponse.<List<RoleResponse>>builder().data(userService.findAll()).build();
    }

    @PutMapping("/lock/{userId}")
    public ApiResponse<AccountResponse> lockAccount (@PathVariable("userId") String userId){
        return ApiResponse.<AccountResponse>builder().data(userService.lockAccount(userId)).build();
    }

    @PutMapping("/unlock/{userId}")
    public ApiResponse<AccountResponse> unLockAccount (@PathVariable("userId") String userId){
        return ApiResponse.<AccountResponse>builder().data(userService.unLockAccount(userId)).build();
    }

    @GetMapping("/find-all-account")
    public ApiResponse<PageResponse<AccountResponse>> findAllAccount (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size){
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .data(userService.findAllAccount(page, size)).build();
    }

    @GetMapping("/search")
    public ApiResponse<PageResponse<AccountResponse>> findAllAccountByKeyword (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam(value = "keyword") String keyword){
        return ApiResponse.<PageResponse<AccountResponse>>builder()
                .data(userService.findAccountByKeyword(page, size, keyword)).build();
    }
}
