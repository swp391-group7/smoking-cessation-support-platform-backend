package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCesProgressRequest {
    private UUID planId;
    private UUID planStepId;
    private String status;
    private String mood;
    private Integer cigarettesSmoked;
    private String note;

}
