package com.kamyesm.filemanagementservice.Provider;

import com.kamyesm.filemanagementservice.Properties.StorageProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioStorageProvider implements StorageProvider {

    private final MinioClient minioClient;
    private final StorageProperties properties;

    public String upload(MultipartFile file , String objectName) throws Exception {


        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return objectName;
    }

    public InputStream download(String objectName) throws Exception {


        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(properties.getBucket())
                        .object(objectName)
                        .build()
        );
    }

}

