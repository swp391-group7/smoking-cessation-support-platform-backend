// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/QuitPlanService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;
import java.util.UUID;

public interface QuitPlanService {
    QuitPlanResponse create(QuitPlanRequest request);
    QuitPlanResponse update(UUID id, QuitPlanRequest request);
    QuitPlanResponse get(UUID id);
    void delete(UUID id);
}