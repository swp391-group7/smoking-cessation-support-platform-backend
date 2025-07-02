package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cessationprogress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CessationProgressDto {
    private UUID id;
    private UUID planId;
    private UUID userId;
    private LocalDate logDate;
    private Integer cigarettesSmoked;
    private String note;
    private String mood;
    private String status;
}

