package com.Swp_391_gr7.smoking_cessation_support_platform_backend.configs;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    public JwtAuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            // Validate token
            if (jwtService.validateToken(token)) {
                UUID userId = jwtService.extractId(token);
                // Tạo Authentication mà không grant role (nếu chưa có)
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            // Nếu token không hợp lệ, SecurityContextHolder vẫn null,
            // cuối cùng sẽ bị Spring Security chặn khi check .authenticated()
        }
        chain.doFilter(request, response);
    }
}
