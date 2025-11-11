package com.dongVu1105.post_service.repository.httpClient;



import com.dongVu1105.post_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "profile-service", url = "${app.services.profile}", configuration = AuthenticationRequestInterceptor.class)
public interface UserProfileClient {
    @GetMapping("/user/{userId}")
    ApiResponse<UserProfileResponse> findById (@PathVariable("userId") String userId);
}
