package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuitPlanDto {
    private UUID id;
    private UUID userId;
    private UUID userSurveyId;
    private LocalDate startDate;
    //private UUID smokeSurveyId;
    private String method;
    private LocalDate targetDate;
    private LocalDateTime createAt;
    private String status;
    private LocalDateTime updatedAt;
    private Integer currentZeroStreak;
    private Integer maxZeroStreak;
}
