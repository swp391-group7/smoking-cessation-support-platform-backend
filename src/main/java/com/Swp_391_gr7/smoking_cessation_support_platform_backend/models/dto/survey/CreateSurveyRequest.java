package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSurveyRequest {
    @NotNull
    private String type_survey;
    @NotNull
    private LocalDateTime createAt;
}
