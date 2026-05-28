package com.kamyesm.filemanagementservice.Provider;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageProvider {
    String upload(MultipartFile file , String object) throws Exception;
    InputStream download(String objectName) throws Exception;
}
