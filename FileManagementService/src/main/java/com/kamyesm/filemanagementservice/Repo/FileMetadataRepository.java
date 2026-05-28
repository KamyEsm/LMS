package com.kamyesm.filemanagementservice.Repo;

import com.kamyesm.filemanagementservice.Entity.FileMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileMetadataRepository extends MongoRepository<FileMetadata, String> {
    List<FileMetadata> findByFileNameContainingAndUserIdAndStatus(String fileName, String userId, String name);

    List<FileMetadata> findByFileNameAndUserIdAndStatus(String fileName, String userId, String name);
}

