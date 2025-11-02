package com.dongVu1105.file_service.controller;

import com.dongVu1105.file_service.dto.ApiResponse;
import com.dongVu1105.file_service.dto.response.FileData;
import com.dongVu1105.file_service.dto.response.FileResponse;
import com.dongVu1105.file_service.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/internal/media")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalFileController {
    FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<FileResponse> upload (@RequestParam("file") MultipartFile multipartFile) throws IOException {
        return ApiResponse.<FileResponse>builder().data(fileService.upload(multipartFile)).build();
    }
}
