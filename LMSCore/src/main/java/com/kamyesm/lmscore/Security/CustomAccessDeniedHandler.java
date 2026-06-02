package com.kamyesm.lmscore.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenKind = null;

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {
            tokenKind = jwtAuth.getToken().getClaimAsString("token_kind");
        }

        if ("service".equals(tokenKind)) {
            LoggerFactory.getLogger(CustomAccessDeniedHandler.class)
                    .warn("Service token denied. path={}", request.getRequestURI());
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        if ("user".equals(tokenKind)) {
            response.getWriter().write("""
                    {"error":"forbidden","message":"You do not have permission to access this section."}
                """);
        } else {
            response.getWriter().write("""
                    {"error":"forbidden","message":"Access denied."}
                """);
        }
    }
}
