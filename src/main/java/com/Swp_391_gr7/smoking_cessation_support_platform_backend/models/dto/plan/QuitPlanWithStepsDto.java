package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.QuitPlanStepDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuitPlanWithStepsDto {
    private UUID id;
    private UUID userId;
    private UUID userSurveyId;
    private LocalDate startDate;
    private LocalDate targetDate;
    private String method;
    private String status;
    private Integer currentZeroStreak;
    private Integer maxZeroStreak;
    private LocalDateTime createAt;
    private List<QuitPlanStepDto> steps;
}

