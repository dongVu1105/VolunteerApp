package com.dongVu1105.post_service.repository.httpClient;

import com.dongVu1105.post_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.response.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${app.services.event}", configuration = AuthenticationRequestInterceptor.class)
public interface EventClient {
    @GetMapping("/user/exist/{userId}/{eventId}")
    ApiResponse<Boolean> isUserInEvent (
            @PathVariable("userId") String userId,
            @PathVariable("eventId") String eventId);

    @GetMapping("/{eventId}")
    ApiResponse<EventResponse> findById (@PathVariable("eventId") String eventId);
}
