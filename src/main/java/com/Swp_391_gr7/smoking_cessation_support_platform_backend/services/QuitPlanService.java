// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/QuitPlanService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;

import java.util.List;
import java.util.UUID;

public interface QuitPlanService {
    QuitPlanDto create(UUID userId, QuitPlanCreateRequest request);
    QuitPlanDto update(UUID id, QuitPlanCreateRequest request);
    QuitPlanDto get(UUID id);
    void delete(UUID id);
    List<QuitPlanResponse> getUserPlans(UUID userId);
}