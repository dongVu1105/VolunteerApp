package com.dongVu1105.identity_service.repository.httpClient;

import com.dongVu1105.identity_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.identity_service.dto.ApiResponse;
import com.dongVu1105.identity_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.identity_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "profile-service",
        url = "${app.services.profile}")
public interface UserProfileClient {
    @PostMapping("/internal/user")
    ApiResponse<UserProfileResponse> create (@RequestBody ProfileCreationRequest request);

    @GetMapping("/internal/find/{username}")
    ApiResponse<UserProfileResponse> findByUsername (@PathVariable("username") String username);
}
