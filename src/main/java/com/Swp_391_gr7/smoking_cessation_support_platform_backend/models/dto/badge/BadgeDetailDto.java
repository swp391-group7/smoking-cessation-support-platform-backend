package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BadgeDetailDto {
    private UUID id;
    private String badgeName;
    private String badgeDescription;
    private String badgeImageUrl;
    private Integer condition;      // ngưỡng
    private LocalDateTime createdAt;
}
