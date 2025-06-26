package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.UpdatePLanStepRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;

import java.util.List;
import java.util.UUID;

public interface QuitPlanStepService {
    List<Quit_Plan_Step> getStepsByPlan(UUID planId);
    Quit_Plan_Step createStep(UUID planId, Quit_Plan_Step step);
    /** Tạo step mặc định (status=draft) với stepNumber tự tăng */
    Quit_Plan_Step createDefaultStep(UUID planId);
    Quit_Plan_Step updateStep(UUID stepId, Quit_Plan_Step step);
    /** Cập nhật step theo stepNumber và set status active */
    Quit_Plan_Step updateStepByNumber(UUID planId, Integer stepNumber, UpdatePLanStepRequest request);
    void deleteStep(UUID stepId);
    /** Xóa step theo stepNumber và dời lại thứ tự */
    void deleteStepByNumber(UUID planId, Integer stepNumber);
    /** Xóa tất cả step có status=draft */
    void deleteAllDraftSteps(UUID planId);
    List<Quit_Plan_Step> getStepsByPlanOrderByNumber(UUID planId);

}
