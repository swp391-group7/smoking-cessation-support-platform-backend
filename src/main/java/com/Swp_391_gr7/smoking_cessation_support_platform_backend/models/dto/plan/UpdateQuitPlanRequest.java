package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
public class UpdateQuitPlanRequest {

    private LocalDate startDate;
    private LocalDate targetDate;

}