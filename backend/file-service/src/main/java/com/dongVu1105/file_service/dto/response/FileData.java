package com.dongVu1105.file_service.dto.response;

import org.springframework.core.io.Resource;

public record FileData(String contentType, Resource resource) {
}
