package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePackageTypeRequest {
    private String description;
    private BigDecimal price;
    private Integer duration;
}

