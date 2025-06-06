package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;

import lombok.*;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSmokeSurveyRequest {
    @NotNull
    private Integer smokeDuration;

    @NotNull
    @Min(0)
    private Integer cigarettesPerDay;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal priceEach;

    @NotNull
    private Boolean triedToQuit;

    private String reasonsCantQuit;

    private String healthStatus;

    @NotBlank
    private String dependencyLevel;

    private String note;
}
