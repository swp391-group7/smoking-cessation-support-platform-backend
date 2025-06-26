package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuitPlanStepDto {
    private UUID id;
    private UUID quitPlanId;
    private Integer stepNumber;
    private LocalDate stepStartDate;
    private LocalDate stepEndDate;
    private Integer targetCigarettesPerDay;
    private String stepDescription;
    private String status;
    private LocalDateTime createAt;

}
