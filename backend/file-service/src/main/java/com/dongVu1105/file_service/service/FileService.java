package com.dongVu1105.file_service.service;

import com.dongVu1105.file_service.dto.request.FileInfo;
import com.dongVu1105.file_service.dto.response.FileData;
import com.dongVu1105.file_service.dto.response.FileResponse;
import com.dongVu1105.file_service.entity.FileManagement;
import com.dongVu1105.file_service.exception.AppException;
import com.dongVu1105.file_service.exception.ErrorCode;
import com.dongVu1105.file_service.mapper.FileManagementMapper;
import com.dongVu1105.file_service.repository.FileManagementRepository;
import com.dongVu1105.file_service.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileService {

    FileManagementRepository fileManagementRepository;
    FileRepository fileRepository;
    FileManagementMapper fileManagementMapper;

    public FileResponse upload (MultipartFile file) throws IOException {
        FileInfo fileInfo = fileRepository.save(file);
        FileManagement fileManagement = fileManagementMapper.toFileManagement(fileInfo);
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        fileManagement.setUserId(userId);
        fileManagementRepository.save(fileManagement);
        return FileResponse.builder()
                .originalFileName(file.getOriginalFilename())
                .url(fileInfo.getUrl()).build();
    }

    public FileData download (String fileName) throws IOException {
        FileManagement fileManagement = fileManagementRepository.findById(fileName)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_EXISTED));
        Resource resource = fileRepository.read(fileManagement);
        return new FileData(fileManagement.getContentType(), resource);
    }
}
