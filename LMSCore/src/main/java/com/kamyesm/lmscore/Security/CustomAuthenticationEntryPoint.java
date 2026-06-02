package com.kamyesm.lmscore.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String path = request.getRequestURI();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        if (path.startsWith("/api/user/")) {
            response.getWriter().write("""
                    {"error":"unauthorized","message":"Your session is not valid or has expired. Please log in again."}
                """);
        }
        else {
            response.getWriter().write("""
                    {"error":"unauthorized","message":"Authentication failed."}
                """);
        }
    }
}
