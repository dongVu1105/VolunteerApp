package com.dongVu1105.file_service.repository;

import com.dongVu1105.file_service.entity.FileManagement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagementRepository extends MongoRepository<FileManagement, String> {
}
