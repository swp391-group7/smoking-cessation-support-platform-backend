package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;

@RequiredArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @Schema(
            description = "User's login username"
            , example = "john_doe"
    )
    private String username;
    @Schema(
            description = "User's password",
            example = "P@ssw0rd!"
    )
    private String password;
}


