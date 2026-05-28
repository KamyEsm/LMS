package com.kamyesm.filemanagementservice.Entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "file_metadata")
@Data
@Builder
public class FileMetadata {
    @Id
    private String id;

    private String objectKey;
    private String fileName;
    private long fileSize;
    private String contentType;
    private String userId;
    private String status;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private List<LocalDateTime> downloadedAtList;

    private LocalDateTime deletedAt;
}
