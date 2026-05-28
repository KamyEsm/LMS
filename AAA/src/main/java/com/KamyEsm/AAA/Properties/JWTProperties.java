package com.KamyEsm.AAA.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Setter
@Getter
public class JWTProperties {
    private String secretKey;
    private long expirationTime;
}
