package com.dongVu1105.identity_service.controller;

import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.AuthenticationRequest;
import com.dongVu1105.identity_service.dto.request.IntrospectRequest;
import com.dongVu1105.identity_service.dto.request.LogoutRequest;
import com.dongVu1105.identity_service.dto.request.RefreshTokenRequest;
import com.dongVu1105.identity_service.dto.response.AuthenticationResponse;
import com.dongVu1105.identity_service.dto.response.IntrospectResponse;
import com.dongVu1105.identity_service.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login (@RequestBody AuthenticationRequest request){
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.authenticate(request)).build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken (@RequestBody RefreshTokenRequest request)
            throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationService.refreshToken(request)).build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout (@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request){
        return ApiResponse.<IntrospectResponse>builder()
                .data(authenticationService.introspect(request)).build();
    }


}
