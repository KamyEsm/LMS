package com.KamyEsm.AAA.Service.JWT;

import com.KamyEsm.AAA.ExceptionHandling.JwtGenerationException;
import com.KamyEsm.AAA.Properties.JWTProperties;
import com.KamyEsm.AAA.Security.SecurityUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class JWTGenerateServiceImp implements JWTGenerateService{

    private final JWTProperties jwt;


    public String generateJWTToken() throws JwtGenerationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("No valid authentication found in Security Context.");
        }

        if (!(authentication.getPrincipal() instanceof SecurityUser user)) {
            throw new IllegalStateException("Authentication principal is not an instance of SecurityUser.");
        }

        try {
            String secretKeyString = jwt.getSecretKey();

            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));

            return Jwts.builder()
                    .subject(user.getUsername())
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + jwt.getExpirationTime()))
                    .claim("userName", user.getUsername())
                    .claim("authorities", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            throw new JwtGenerationException("Failed to generate JWT token: " + e.getMessage());
        }
    }


}
