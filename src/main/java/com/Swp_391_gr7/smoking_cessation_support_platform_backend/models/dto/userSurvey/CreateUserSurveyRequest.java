package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey;

import lombok.*;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserSurveyRequest {
    @NotNull
    private String smokeDuration;

    @NotNull
    @Min(0)
    private Integer cigarettesPerDay;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal priceEach;

    @NotNull
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
    @NotBlank
    private Integer dependencyLevel;

    private String note;
}
