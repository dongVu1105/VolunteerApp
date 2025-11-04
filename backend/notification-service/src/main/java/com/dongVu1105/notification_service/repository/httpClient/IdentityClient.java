package com.dongVu1105.notification_service.repository.httpClient;



import com.dongVu1105.notification_service.dto.ApiResponse;
import com.dongVu1105.notification_service.dto.request.IntrospectRequest;
import com.dongVu1105.notification_service.dto.response.IntrospectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "identity-service", url = "${app.services.identity.url}")
public interface IdentityClient {
    @PostMapping("/auth/introspect")
    ApiResponse<IntrospectResponse> introspect (@RequestBody IntrospectRequest request);
}
