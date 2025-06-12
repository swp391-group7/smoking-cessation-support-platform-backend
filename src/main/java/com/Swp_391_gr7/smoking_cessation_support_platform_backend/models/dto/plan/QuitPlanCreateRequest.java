package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class QuitPlanCreateRequest {

    private LocalDate startDate;

    private String method;
    private LocalDate targetDate;

    private String status;
}
