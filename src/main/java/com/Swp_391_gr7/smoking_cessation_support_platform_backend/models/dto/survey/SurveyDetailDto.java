// SurveyDetailDto.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SurveyDetailDto {
    private UUID id;
    private UUID userId;
    private String typeSurvey;
    private LocalDateTime createAt;
    private List<QuestionWithAnswersDto> questions;
}
