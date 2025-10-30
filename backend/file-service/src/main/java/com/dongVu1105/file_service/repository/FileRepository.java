package com.dongVu1105.file_service.repository;

import com.dongVu1105.file_service.dto.request.FileInfo;
import com.dongVu1105.file_service.entity.FileManagement;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Repository
public class FileRepository {
    @NonFinal
    @Value("${app.file.download-prefix}")
    private String urlPrefix;

    @NonFinal
    @Value("${app.file.storage-dir}")
    private String storageDirRelative;

    public FileInfo save (MultipartFile file) throws IOException {
        Path folder = Paths.get(storageDirRelative).toAbsolutePath().normalize();

        // Tạo thư mục nếu chưa tồn tại
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String fileName = (fileExtension == null)
                ? UUID.randomUUID().toString()
                : UUID.randomUUID() + "." + fileExtension;

        Path filePath = folder.resolve(fileName);

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return FileInfo.builder()
                .name(fileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .md5Checksum(DigestUtils.md5DigestAsHex(file.getInputStream()))
                .path(filePath.toString())
                .url(urlPrefix + fileName)
                .build();
    }

    public Resource read (FileManagement fileManagement) throws IOException {
        var data = Files.readAllBytes(Path.of(fileManagement.getPath()));
        return new ByteArrayResource(data);
    }
}
