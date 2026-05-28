package com.kamyesm.filemanagementservice.Properties;

import com.kamyesm.filemanagementservice.Enum.StorageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
@Getter
@Setter
public class StorageProperties {
    private StorageType storageType;
    private String url;
    private String accessKey;
    private String secretKey;
    private String bucket;
}

