package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTypeDto {
    private UUID id;
    private String name;
    private String description;
    private String des1;
    private String des2;
    private String des3;
    private String des4;
    private String des5;
    private BigDecimal price;
    private Integer duration;
    private LocalDateTime createAt;
}
