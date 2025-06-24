package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateMembershipPackageRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
}
