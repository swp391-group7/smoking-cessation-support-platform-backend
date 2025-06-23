package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerDto {
    private UUID id;
    private UUID questionId;
    private String answerText;
    private Integer point;
    private LocalDateTime createdAt;
}
