package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Setter
@Getter
public class QuitPlanResponse {
    private UUID id;
    private UUID userId;
    private LocalDate startDate;
    private String goal;
    private String note;
}