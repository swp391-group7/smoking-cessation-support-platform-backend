package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;

import java.util.List;
import java.util.UUID;

public interface QuitPlanStepService {
    /**
     * Lấy tất cả các bước của một plan, sắp xếp theo ngày bắt đầu.
     */
    List<Quit_Plan_Step> getStepsByPlan(UUID planId);

    /**
     * Tạo mới một bước cho Quit Plan
     */
    Quit_Plan_Step createStep(UUID planId, Quit_Plan_Step step);

    /**
     * Cập nhật thông tin một bước
     */
    Quit_Plan_Step updateStep(UUID stepId, Quit_Plan_Step step);

    /**
     * Xóa một bước theo ID
     */
    void deleteStep(UUID stepId);

    /**
     * Lấy tất cả các bước của một plan, sắp xếp theo số thứ tự bước (stepNumber).
     */
    List<Quit_Plan_Step> getStepsByPlanOrderByNumber(UUID planId);

}
