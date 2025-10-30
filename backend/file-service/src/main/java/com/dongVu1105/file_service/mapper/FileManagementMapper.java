package com.dongVu1105.file_service.mapper;

import com.dongVu1105.file_service.dto.request.FileInfo;
import com.dongVu1105.file_service.entity.FileManagement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileManagementMapper {
    @Mapping(source = "name", target = "id")
    FileManagement toFileManagement (FileInfo fileInfo);
}
