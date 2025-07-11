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
    private String name;
    private String description;
    private String des1;
    private String des2;
    private String des3;
    private String des4;
    private String des5;
    private BigDecimal price;
    private Integer duration;
}
