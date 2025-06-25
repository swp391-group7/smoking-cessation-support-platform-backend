package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoachDto {
    private UUID userId;
    private String bio;
    private String qualification;
    private BigDecimal avgRating;
}
