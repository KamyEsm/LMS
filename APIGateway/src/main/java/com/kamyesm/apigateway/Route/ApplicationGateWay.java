package com.kamyesm.apigateway.Route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationGateWay {


    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path(
                                "/login",
                                "/css/loginpagestyle.css",
                                "/oauth2/**",
                                "/userinfo",
                                "/connect/logout",
                                "/.well-known/openid-configuration"
                                )
                        .uri("http://localhost:8081"))
                .build();
    }


}
