package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSmokeSurveyRequest;

import java.util.UUID;

public interface SmokeSurveyService {

    /**
     * Tạo mới khảo sát hút thuốc cho user
     */
    SmokeSurveyDto createSurvey(UUID userId, CreateSmokeSurveyRequest request);

    /**
     * Lấy khảo sát theo userId, ném lỗi nếu không tìm thấy
     */
    SmokeSurveyDto getSurvey(UUID userId);

    /**
     * Cập nhật khảo sát hiện có, ném lỗi nếu không tìm thấy
     */
    SmokeSurveyDto updateSurvey(UUID userId, UpdateSmokeSurveyRequest request);

    /**
     * Xóa khảo sát theo userId, ném lỗi nếu không tìm thấy
     */
    void deleteSurvey(UUID userId);

    SmokeSurveyDto getSurveyById(UUID surveyId);
}
