package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePlanStepRequest {
    private LocalDate stepStartDate;
    private LocalDate stepEndDate;
    private Integer stepNumber;
    private Integer targetCigarettesPerDay;
    private String stepDescription;
}
