package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack;

import lombok.Data;

@Data
public class SystemFeedbackUpdateDTO {
    private Short rating;
    private String comment;
}