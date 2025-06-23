// QuestionWithAnswersDto.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.AnswerDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class QuestionWithAnswersDto {
    private UUID id;
    private String content;
    private LocalDateTime createAt;
    private List<AnswerDto> answers;
}
