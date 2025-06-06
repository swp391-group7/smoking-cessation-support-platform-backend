package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.survey;

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
public class UpdateSmokeSurveyRequest {

    @NotNull
    private Integer smokeDuration;

    @Min(0)
    private Integer cigarettesPerDay;

    @DecimalMin("0.0")
    private BigDecimal priceEach;

    private Boolean triedToQuit;

    private String reasonsCantQuit;

    private String healthStatus;

    private String dependencyLevel;

    private String note;
}