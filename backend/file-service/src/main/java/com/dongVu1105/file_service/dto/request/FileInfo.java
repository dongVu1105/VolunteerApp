package com.dongVu1105.file_service.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileInfo {
    String name;
    String contentType;
    long size;
    String md5Checksum;
    String path;
    String url;
}
