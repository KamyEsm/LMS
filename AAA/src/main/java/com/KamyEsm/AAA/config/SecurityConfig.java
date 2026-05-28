package com.KamyEsm.AAA.config;

import com.KamyEsm.AAA.Filter.JWTTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTTokenValidatorFilter jwtTokenValidatorFilter;


    @Bean
    public SecurityFilterChain setSecurityConfig(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(s -> s.requestMatchers("/user/**").authenticated()
                        .requestMatchers("/role/**").authenticated()
                        .requestMatchers("/permission/**").authenticated()
                        .anyRequest().permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // JWTتداخل با
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public FilterRegistrationBean<JWTTokenValidatorFilter> tenantFilterRegistration(JWTTokenValidatorFilter filter) {
        FilterRegistrationBean<JWTTokenValidatorFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false); // جلوی ثبت خودکار در کل Servlet Container را می‌گیرد
        return registration;
    }

}
