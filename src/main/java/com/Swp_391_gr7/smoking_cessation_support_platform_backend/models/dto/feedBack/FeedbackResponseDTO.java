package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.FeedbackTarget;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO trả về cho client sau khi thao tác hoặc khi lấy danh sách Feedback.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackResponseDTO {
    private UUID id;
    private UUID userId;
    private FeedbackTarget targetType;
    private UUID membershipPkgId;
    private Short rating;
    private String comment;
    private LocalDateTime createdAt;
}