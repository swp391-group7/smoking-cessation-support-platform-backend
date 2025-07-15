// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/models/dto/feedBack/CoachFeedbackRequestDTO.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachFeedbackRequestDTO {

    @NotNull
    @Min(1) @Max(5)
    private Short rating;

    @NotNull
    private String comment;
}
