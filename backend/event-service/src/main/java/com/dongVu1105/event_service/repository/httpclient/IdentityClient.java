package com.dongVu1105.event_service.repository.httpclient;

import com.dongVu1105.event_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-service", url = "${app.services.identity}",
        configuration = AuthenticationRequestInterceptor.class)
public interface IdentityClient {
    @GetMapping("/user/find-by-id/{id}")
    ApiResponse<UserResponse> findById (@PathVariable("id") String id);
}
