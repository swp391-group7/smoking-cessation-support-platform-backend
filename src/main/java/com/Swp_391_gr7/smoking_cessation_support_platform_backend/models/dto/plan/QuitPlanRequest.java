package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;

import java.time.LocalDate;
import java.util.UUID;
public class QuitPlanRequest {
    private UUID userId;
    private LocalDate startDate;
    private String goal;
    private String note;
}
