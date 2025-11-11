package com.dongVu1105.post_service.repository.httpClient;


import com.dongVu1105.post_service.configuration.AuthenticationRequestInterceptor;
import com.dongVu1105.post_service.configuration.FeignConfiguration;
import com.dongVu1105.post_service.dto.ApiResponse;
import com.dongVu1105.post_service.dto.response.FileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "file-service",
        url = "${app.services.file}",
        configuration = {AuthenticationRequestInterceptor.class, FeignConfiguration.class})
public interface FileClient {
    @PostMapping(value = "/internal/media/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<FileResponse> uploadMedia (@RequestPart("file") MultipartFile file);
}
