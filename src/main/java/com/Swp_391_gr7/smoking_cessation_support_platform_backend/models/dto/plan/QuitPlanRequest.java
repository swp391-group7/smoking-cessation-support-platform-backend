package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class QuitPlanRequest {
    private UUID id;
    private UUID userId;
    private UUID coachId;
    private LocalDate startDate;
    private UUID smokeSurveyId;
    private String method;
    private LocalDate StartDate;
    private LocalDate targetDate;
    private LocalDateTime createAt;
    private String status;
}
