package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;

import java.util.List;
import java.util.UUID;

public interface QuitPlanService {
    QuitPlanDto create(UUID userId,QuitPlanCreateRequest request);
    QuitPlanDto update(UUID id, QuitPlanCreateRequest request, UUID userId);
    QuitPlanDto getById(UUID id);
    void delete(UUID id, UUID userId);
    List<QuitPlanDto> getAll();
    List<QuitPlanDto> searchByMethodOrStatus(String method, String status, UUID userId);
    QuitPlanDto generatePlanFromSurvey(UUID userId, UUID smokeSurveyId);
    List<QuitPlanDto> getByUserId(UUID userId);
}