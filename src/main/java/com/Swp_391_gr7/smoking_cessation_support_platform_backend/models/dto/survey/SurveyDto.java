package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDto {
    private UUID id;
    private UUID userId;
    private String type_survey;
    private LocalDateTime createAt;
}
