package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateSurveyRequest {
    private String type_survey;
}
