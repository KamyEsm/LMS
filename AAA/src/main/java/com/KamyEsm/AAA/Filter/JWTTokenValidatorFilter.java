package com.KamyEsm.AAA.Filter;

import com.KamyEsm.AAA.Properties.JWTProperties;
import com.KamyEsm.AAA.Security.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */

    private final JWTProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println(">>> JWT Filter IS RUNNING!");
        String authenticationHeader = request.getHeader("Authorization");
        if(null != authenticationHeader && authenticationHeader.startsWith("Bearer ")) {
            String jwt = authenticationHeader.substring(7);
            try {
                Environment env = getEnvironment();
                String secret = jwtProperties.getSecretKey();
                SecretKey secretKey = Keys.hmacShaKeyFor(
                        io.jsonwebtoken.io.Decoders.BASE64.decode(secret)
                );
                Claims claims = Jwts.parser().verifyWith(secretKey)
                        .build().parseSignedClaims(jwt).getPayload();
                String username = String.valueOf(claims.get("userName"));
                List<Authority> authoritiesList = (List)(claims.get("authorities"));
                String authorities = authoritiesList.toString().substring(1,authoritiesList.toString().length()-1);
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception exception) {
                System.out.println("JWT Validation failed: " + exception.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new BadCredentialsException("JWT Validation failed: " + exception.getMessage());
            }
        }
        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/login");
    }

}