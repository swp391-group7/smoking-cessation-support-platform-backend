package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePLanStepRequest {
    private LocalDate stepStartDate;
    private LocalDate stepEndDate;
    private Integer stepNumber;
    private Integer targetCigarettesPerDay;
    private String stepDescription;
    private String status;
}
