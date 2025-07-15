package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanWithStepsDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.UpdateQuitPlanRequest;
import java.util.List;
import java.util.UUID;

public interface QuitPlanService {
    QuitPlanDto create(UUID userId,QuitPlanCreateRequest request);
//    QuitPlanDto update(UUID id, QuitPlanCreateRequest request, UUID userId);
    QuitPlanDto getById(UUID id);
    void delete(UUID id, UUID userId);
    List<QuitPlanDto> getAll();
    List<QuitPlanDto> searchByMethodOrStatus(String method, String status, UUID userId);
    QuitPlanWithStepsDto generatePlanFromSurvey(UUID userId, UUID smokeSurveyId);

    QuitPlanDto getActivePlanByUserId(UUID userId);



    /**
     * Tạo ngay lập tức một kế hoạch.
     * Phương thức, startDate và targetDate đều được tự động set.
     */
    QuitPlanDto createImmediatePlan(UUID userId);

    /**
     * Tạo một kế hoạch gradual ở trạng thái draft (chưa active, chưa có step).
     * Phương thức, startDate và targetDate đều được tự động set.
     */
    QuitPlanDto createDraftPlan(UUID userId);

    /**
     * Xóa tất cả các kế hoạch ở trạng thái draft của user.
     */
    void deleteAllDrafts(UUID userId);

    /**
     * Cập nhật kế hoạch draft gần nhất của user.
     */
    QuitPlanDto updateLatestDraft(UUID userId, UpdateQuitPlanRequest request);
    Integer getCurrentZeroStreak(UUID userId);
    Integer getMaxZeroStreak(UUID userId);

    QuitPlanDto activatePlan(UUID userId, UUID planId);

}