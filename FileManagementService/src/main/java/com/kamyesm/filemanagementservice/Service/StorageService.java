package com.kamyesm.filemanagementservice.Service;

import com.kamyesm.filemanagementservice.DTO.FileDeleteResponse;
import com.kamyesm.filemanagementservice.DTO.FileDownloadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String upload(MultipartFile file, String userId) throws Exception;

    FileDownloadResponse download(String fileName, String userId) throws Exception;

    FileDeleteResponse delete(String fileName , String UserId);
}
