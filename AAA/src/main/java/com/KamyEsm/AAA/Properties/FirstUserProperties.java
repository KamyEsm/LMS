package com.KamyEsm.AAA.Properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "first-user")
@Component
@Getter
@Setter
public class FirstUserProperties {
    private String username;
    private String password;
}
