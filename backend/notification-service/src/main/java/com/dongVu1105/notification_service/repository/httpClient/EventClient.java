package com.dongVu1105.notification_service.repository.httpClient;

import com.dongVu1105.notification_service.configuration.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "event-service",
        url = "${app.services.event.url}",
        configuration = AuthenticationRequestInterceptor.class)
public interface EventClient {

}
