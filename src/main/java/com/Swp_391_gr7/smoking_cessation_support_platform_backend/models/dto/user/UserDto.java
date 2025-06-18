package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user;

import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private UUID id;
    private String username;
    private String password;
    private String email;
    private String providerId;
    private String fullName;
    private String phoneNumber;
    private LocalDate dob;
    private String avtarPath;
    private Boolean preStatus;
    private LocalDateTime createdAt;
}
