package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey;

import lombok.*;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

/**
 * Dùng khi cập nhật survey (cho phép cập nhật hầu hết các trường)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserSurveyRequest {

    @NotNull
    private Integer smokeDuration;

    @Min(0)
    private Integer cigarettesPerDay;

    @DecimalMin("0.0")
    private BigDecimal priceEach;

    private Boolean triedToQuit;


    private String healthStatus;

    private String a1;
    private String a2;
    private String a3;
    private String a4;
    private String a5;
    private String a6;
    private String a7;
    private String a8;


    private Integer dependencyLevel;

    private String note;
}