package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionRequest {
    private String content;
}
