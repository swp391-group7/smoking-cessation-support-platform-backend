package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@RequiredArgsConstructor
@Getter
@Setter
public class SigninRequest {
    @Schema(
            description = "User's login username",
            example = "john_doe"
    )
    private String username;
    @Schema(
            description = "User's password",
            example = "P@ssw0rd!"
    )
    private String password;
    @Schema(
            description ="Full name of the user",
            example = "John Doe"
    )
    private String fullName;
    @Schema(
            description = "Email of the user",
            example = "email@gmail.com"
    )
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private String sex;
}
