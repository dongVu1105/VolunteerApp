package com.dongVu1105.file_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document("file-management")
public class FileManagement {
    @MongoId
    String id;
    String userId;
    String md5checksum;
    String contentType;
    long size;
    String path;
}
