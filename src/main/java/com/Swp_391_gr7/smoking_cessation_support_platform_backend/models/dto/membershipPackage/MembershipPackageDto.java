package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MembershipPackageDto {
    private UUID id;
    private UUID userId;
    private UUID packagetTypeId;
    private UUID coachId; // Có thể null nếu không có coach đồng hành
    private String packageTypeName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean isActive;
    private LocalDateTime createdAt;
}
