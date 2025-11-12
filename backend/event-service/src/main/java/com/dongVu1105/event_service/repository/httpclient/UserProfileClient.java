package com.dongVu1105.event_service.repository.httpclient;


import com.dongVu1105.event_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.event_service.configuration.FeignConfiguration;
import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.request.GetProfileRequest;
import com.dongVu1105.event_service.dto.request.ProfileCreationRequest;
import com.dongVu1105.event_service.dto.response.PageResponse;
import com.dongVu1105.event_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "profile-service", url = "${app.services.profile}",
        configuration = {AuthenticationRequestInterceptor.class, FeignConfiguration.class})
public interface UserProfileClient {
    @GetMapping("/user/{userId}")
    ApiResponse<UserProfileResponse> findById (@PathVariable("userId") String userId);

    @PostMapping(value = "/internal/userId-list", consumes = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<List<UserProfileResponse>> findAllByUserIdList (@RequestBody GetProfileRequest request);
}
