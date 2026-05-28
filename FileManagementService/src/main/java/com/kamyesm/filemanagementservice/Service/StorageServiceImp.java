package com.kamyesm.filemanagementservice.Service;

import com.kamyesm.filemanagementservice.DTO.FileDeleteResponse;
import com.kamyesm.filemanagementservice.DTO.FileDownloadResponse;
import com.kamyesm.filemanagementservice.Entity.FileMetadata;
import com.kamyesm.filemanagementservice.Enum.Status;
import com.kamyesm.filemanagementservice.ExceptionHandeling.ResourceNotFoundException;
import com.kamyesm.filemanagementservice.Provider.StorageProvider;
import com.kamyesm.filemanagementservice.Repo.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImp implements StorageService {

    private final StorageProvider storageProvider;
    private final FileMetadataRepository repository;

    public String upload(MultipartFile file, String userId) throws Exception {

        String objectKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

        FileMetadata metadata = FileMetadata.builder()
                .objectKey(objectKey)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .userId(userId)
                .status(Status.PENDING.name())
                .build();

        repository.save(metadata);
        storageProvider.upload(file , objectKey);

        metadata.setStatus(Status.COMPLETED.name());
        repository.save(metadata);

        return objectKey;
    }

    @Override
    public FileDownloadResponse download(String fileName, String userId) throws Exception {

        FileMetadata fileMetadata = find(fileName , userId);

        List<LocalDateTime> list = fileMetadata.getDownloadedAtList();
        if(list == null) list = new ArrayList<>();

        String objectKey = fileMetadata.getObjectKey();
        InputStream stream = storageProvider.download(objectKey);

        return new FileDownloadResponse( new InputStreamResource(stream) , fileMetadata.getFileName() , fileMetadata.getContentType());
    }

    @Override
    public FileDeleteResponse delete(String fileName , String UserId) {
        FileMetadata fileMetadata = find(fileName , UserId);
        fileMetadata.setStatus(Status.Deleted.name());
        fileMetadata.setDeletedAt(LocalDateTime.now());
        repository.save(fileMetadata);
        return new FileDeleteResponse(fileMetadata.getFileName());
    }

    private FileMetadata find(String fileName, String userId){
        List<FileMetadata> list = repository.findByFileNameContainingAndUserIdAndStatus(fileName,userId,Status.COMPLETED.name());
        if(list.isEmpty()) throw new ResourceNotFoundException("file not found");
        int index = 0;
        LocalDateTime near = list.get(index).getCreatedAt();
        for (int i = 1 ; i < list.size() ; i++){
            if(near.isBefore(list.get(i).getCreatedAt())) {
                near = list.get(i).getCreatedAt();
                index = i;
            }
        }
        FileMetadata fileMetadata = list.get(index);
        if(fileMetadata.getStatus().equals(Status.Deleted.name())) throw new ResourceNotFoundException("file not found");
        return fileMetadata;
    }

}
