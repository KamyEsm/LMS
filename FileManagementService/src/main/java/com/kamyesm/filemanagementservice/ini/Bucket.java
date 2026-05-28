package com.kamyesm.filemanagementservice.ini;

import com.kamyesm.filemanagementservice.Conf.MinioConfig;
import com.kamyesm.filemanagementservice.Properties.StorageProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Bucket {
    private final MinioClient minioClient;
    private final StorageProperties properties;

    @PostConstruct
    public void init() throws Exception {

        boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(properties.getBucket())
                        .build()
        );

        if (!found) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(properties.getBucket())
                            .build()
            );
        }
    }

}
