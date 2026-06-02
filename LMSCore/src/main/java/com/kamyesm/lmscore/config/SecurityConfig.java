package com.kamyesm.lmscore.config;

import com.kamyesm.lmscore.Security.CustomAccessDeniedHandler;
import com.kamyesm.lmscore.Security.CustomAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private static final String userTokenKind = "user";
    private static final String serviceTokenKind = "service";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/**").hasAuthority("TOKEN_USER")
                        .requestMatchers("/api/internal/**").hasAuthority("TOKEN_SERVICE")
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            String tokenKind = jwt.getClaimAsString("token_kind");
            if (userTokenKind.equals(tokenKind)) {
                authorities.add(new SimpleGrantedAuthority("TOKEN_USER"));
            } else if (serviceTokenKind.equals(tokenKind)) {
                authorities.add(new SimpleGrantedAuthority("TOKEN_SERVICE"));
            }

            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles != null) {
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
            }

            String scope = jwt.getClaimAsString("scope");
            if (scope != null && !scope.isBlank()) {
                Arrays.stream(scope.split(" "))
                        .filter(s -> !s.isBlank())
                        .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                        .forEach(authorities::add);
            }

            return authorities;
        });
        return converter;
    }

}

