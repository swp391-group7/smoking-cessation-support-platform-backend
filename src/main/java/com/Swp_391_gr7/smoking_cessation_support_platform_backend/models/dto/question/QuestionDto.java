package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionDto {
    private UUID id;
    private UUID surveyId;
    private String content;
    private LocalDateTime createdAt;
}
