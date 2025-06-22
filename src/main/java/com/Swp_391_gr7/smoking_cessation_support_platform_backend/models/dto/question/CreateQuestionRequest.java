package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateQuestionRequest {
    private String content;

}
