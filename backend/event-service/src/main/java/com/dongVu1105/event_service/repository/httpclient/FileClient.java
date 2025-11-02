package com.dongVu1105.event_service.repository.httpclient;

import com.dongVu1105.event_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.event_service.configuration.FeignConfiguration;
import com.dongVu1105.event_service.dto.ApiResponse;
import com.dongVu1105.event_service.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-service",
        url = "${app.services.file}",
        configuration = {AuthenticationRequestInterceptor.class, FeignConfiguration.class})
public interface FileClient {
    @PostMapping(value = "/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadMedia (@RequestPart("file") MultipartFile file);
}
