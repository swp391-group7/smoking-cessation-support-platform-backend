package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.CreateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UserSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UpdateUserSurveyRequest;

import java.util.List;
import java.util.UUID;

public interface UseSurveyService {

    /**
     * Tạo mới khảo sát hút thuốc cho user
     */
    UserSurveyDto createSurvey(UUID userId, CreateUserSurveyRequest request);

    /**
     * Lấy khảo sát theo userId, ném lỗi nếu không tìm thấy
     */
    UserSurveyDto getSurvey(UUID userId);

    /**
     * Cập nhật khảo sát hiện có, ném lỗi nếu không tìm thấy
     */
    UserSurveyDto updateSurvey(UUID userId, UpdateUserSurveyRequest request);

    /**
     * Xóa khảo sát theo userId, ném lỗi nếu không tìm thấy
     */
    void deleteSurvey(UUID userId);

    UserSurveyDto getSurveyById(UUID surveyId);


    UserSurveyDto getFirstSurveyOfUser(UUID userId);

    List<UserSurveyDto>getAllSurveyOfUser(UUID userId);
}
