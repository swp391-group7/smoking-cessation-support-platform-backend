package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.role;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRoleRequest {
    private String role;
}
