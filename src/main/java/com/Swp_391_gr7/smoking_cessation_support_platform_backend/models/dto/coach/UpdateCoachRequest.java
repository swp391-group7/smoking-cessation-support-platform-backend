package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCoachRequest {
    private UUID userId;
    private String bio;
    private String qualification;
    private BigDecimal avgRating;
}
