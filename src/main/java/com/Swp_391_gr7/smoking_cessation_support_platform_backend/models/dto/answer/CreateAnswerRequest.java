package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@Builder
public class CreateAnswerRequest {
    private String answerText;
    private Integer point;
    private LocalDateTime createdAt;
}
