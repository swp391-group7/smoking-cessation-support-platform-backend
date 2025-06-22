package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String password;

    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDate dob;
    private String avtarPath;
}
