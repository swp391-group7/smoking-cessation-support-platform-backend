package com.Swp_391_gr7.smoking_cessation_support_platform_backend.interceptors;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private final JWTService jwtService;

    public JWTInterceptor(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            boolean methodProtected = hm.getMethodAnnotation(JWTProtected.class) != null;
            boolean classProtected = hm.getBeanType().getAnnotation(JWTProtected.class) != null;

            if (methodProtected || classProtected) {
                String header = request.getHeader("Authorization");
                if (header == null || !header.startsWith("Bearer ")) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
                    return false;
                }
                String token = header.substring(7);

                if (!jwtService.validateToken(token)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                    return false;
                }

                UUID userId = jwtService.extractId(token);
                request.setAttribute("userId", userId);
            }
        }
        return true;
    }
}
