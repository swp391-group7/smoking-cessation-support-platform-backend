package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank private String username;
    @NotBlank private String password;
    @NotBlank private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDate dob;
    private String avtarPath;
    private String sex;
    @NotBlank
    private String roleName;
}
