package com.KamyEsm.AAA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableConfigurationProperties
@EnableMethodSecurity
public class AAAApplication {
	public static void main(String[] args) {
		SpringApplication.run(AAAApplication.class, args);
	}
}
