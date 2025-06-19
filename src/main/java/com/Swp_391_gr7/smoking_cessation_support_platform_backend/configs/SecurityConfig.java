package com.Swp_391_gr7.smoking_cessation_support_platform_backend.configs;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.jwt.JWTService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTService jwtService;

    public SecurityConfig(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService);

        http
                // Tắt CSRF vì chỉ dùng token
                .csrf(AbstractHttpConfigurer::disable)
                // Stateless session: không dùng session truyền thống
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Cấu hình authorize requests
                .authorizeHttpRequests(auth -> auth
                        // Cho phép không cần auth với các endpoint public
                        .requestMatchers(
                                "/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/blogs/**", // Thêm đường dẫn cho blog
                                "/error" // Thêm đường dẫn cho lỗi

                        ).permitAll()
                        // Tất cả request khác đều cần authentication
                        .anyRequest().authenticated()
                )
                // Thêm filter parse JWT vào trước filter mặc định UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
