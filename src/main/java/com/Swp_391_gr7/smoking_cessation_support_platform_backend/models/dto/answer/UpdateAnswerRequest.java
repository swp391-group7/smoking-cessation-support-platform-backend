package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAnswerRequest {
    private String answerText;
    private Integer point;
}
