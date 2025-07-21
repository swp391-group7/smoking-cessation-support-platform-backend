// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/feedBack/FeedBackService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.feedBack;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.CoachFeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface FeedBackService {
    FeedbackResponseDTO createFeedBack(FeedbackRequestDTO dto);
    FeedbackResponseDTO updateFeedBack(UUID id, FeedbackRequestDTO dto);
    void deleteFeedBack(UUID id);
    FeedbackResponseDTO getFeedBackById(UUID id);
    List<FeedbackResponseDTO> getAllFeedBack();
    List<FeedbackResponseDTO> getFeedBackByUser(UUID userId);
    List<FeedbackResponseDTO> getFeedBackByTargetType(String targetType);
    List<FeedbackResponseDTO> getFeedBackByCoach(UUID coachId);
    FeedbackResponseDTO feedbackCoach(UUID userId, CoachFeedbackRequestDTO dto);


    // --- System feedback API ---
    /**
     * Tạo 1 feedback hệ thống cho user hiện tại.
     * Trả về avg rating của hệ thống.
     */
    BigDecimal createSystemFeedback(UUID userId, FeedbackRequestDTO dto);

    /**
     * Cập nhật 1 feedback hệ thống.
     * Trả về avg rating của hệ thống.
     */
    FeedbackResponseDTO updateSystemFeedback(UUID userId, FeedbackRequestDTO dto);
    FeedbackResponseDTO updateCoachFeedback(UUID userId, FeedbackRequestDTO dto);
    /**
     * Tính avg rating của tất cả feedback hệ thống.
     */
    BigDecimal computeSystemAvgRating();
}
