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
@RequestMapping("/media")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    // Lấy file ảnh
    @GetMapping("/download/{fileName}")
    ResponseEntity<Resource> download(@PathVariable String fileName) throws IOException {
        FileData fileData = fileService.download(fileName);
        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, fileData.contentType())
                .body(fileData.resource());
    }
}
