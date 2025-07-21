package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.FeedbackTarget;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO để client gửi lên khi tạo hoặc cập nhật Feedback.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestDTO {
    @NotNull
    private UUID userId;

    @NotNull
    private FeedbackTarget targetType;

    /**
     * Chỉ cần truyền membershipPkgId khi targetType=COACH
     */
    private UUID membershipPkgId;

    @NotNull
    @Min(1) @Max(5)
    private Short rating;

    @NotNull
    private String comment;
}
