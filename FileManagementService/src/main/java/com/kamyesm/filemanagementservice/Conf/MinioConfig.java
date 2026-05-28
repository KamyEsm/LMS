package com.kamyesm.filemanagementservice.Conf;

import com.kamyesm.filemanagementservice.Properties.StorageProperties;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MinioConfig {

    @Bean
    @ConditionalOnProperty(name = "storage.storage-type", havingValue = "MINIO")
    public MinioClient minioClient(StorageProperties props) {
        return MinioClient.builder()
                .endpoint(props.getUrl())
                .credentials(props.getAccessKey(), props.getSecretKey())
                .build();
    }
}

