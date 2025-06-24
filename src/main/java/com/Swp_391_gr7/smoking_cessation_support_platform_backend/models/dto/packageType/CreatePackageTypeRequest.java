package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePackageTypeRequest {
    private String description;
    private BigDecimal price;
    private Integer duration;
}
