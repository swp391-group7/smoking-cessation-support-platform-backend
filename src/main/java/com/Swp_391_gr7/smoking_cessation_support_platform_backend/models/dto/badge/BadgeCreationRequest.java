package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeCreationRequest {
    private String badgeName;
    private String badgeDescription;
    private String badgeImageUrl;
    private Integer condition;
}
