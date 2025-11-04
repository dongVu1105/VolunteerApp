package com.dongVu1105.notification_service.service;



import com.dongVu1105.notification_service.dto.request.IntrospectRequest;
import com.dongVu1105.notification_service.dto.response.IntrospectResponse;
import com.dongVu1105.notification_service.repository.httpClient.IdentityClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public IntrospectResponse introspect (IntrospectRequest request){
        IntrospectResponse introspectResponse = identityClient.introspect(request).getData();
        if(Objects.isNull(introspectResponse)){
            return IntrospectResponse.builder().valid(false).build();
        }
        return introspectResponse;
    }
}
