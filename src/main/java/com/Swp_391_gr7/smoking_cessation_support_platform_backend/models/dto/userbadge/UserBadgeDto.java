package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBadgeDto {
    private UUID id;
    private UUID userId;
    private BadgeDto badge;
    private LocalDateTime achievedAt;
}