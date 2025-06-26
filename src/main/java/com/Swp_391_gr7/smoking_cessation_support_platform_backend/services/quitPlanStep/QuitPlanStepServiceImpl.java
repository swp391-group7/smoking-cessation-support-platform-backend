package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuitPlanStepServiceImpl implements QuitPlanStepService{
    @Autowired
    private QuitPlanStepRepository stepRepo; // Repo cho các bước

    @Autowired
    private QuitPlanRepository planRepo; // Repo cho Quit Plan chính

    @Override
    public List<Quit_Plan_Step> getStepsByPlan(UUID planId) {
        // Trả về danh sách bước, đã sắp xếp theo ngày bắt đầu
        return stepRepo.findByPlanIdOrderByStepStartDateAsc(planId);
    }

    @Override
    @Transactional
    public Quit_Plan_Step createStep(UUID planId, Quit_Plan_Step step) {
        // Tìm Plan theo ID
        Quit_Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));

        // Kiểm tra ngày bắt đầu và kết thúc của step nằm trong khoảng của plan
        LocalDate start = step.getStepStartDate();
        LocalDate end = step.getStepEndDate();
        if (start.isBefore(plan.getStartDate()) || end.isAfter(plan.getTargetDate())) {
            throw new IllegalArgumentException("Step dates must lie within plan start and target dates");
        }

        // Xác định stepNumber: nếu chưa có bước, là 1; ngược lại là max(stepNumber) + 1
        List<Quit_Plan_Step> existingSteps = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        int nextNumber = existingSteps.stream()
                .map(Quit_Plan_Step::getStepNumber)
                .max(Comparator.naturalOrder())
                .map(n -> n + 1)
                .orElse(1);
        step.setStepNumber(nextNumber);

        // Gán Plan và lưu bước
        step.setPlan(plan);
        Quit_Plan_Step saved = stepRepo.save(step);

        // Điều chỉnh lại ngày của Plan dựa trên các step
        adjustPlanBounds(plan);
        return saved;
    }

    @Override
    @Transactional
    public Quit_Plan_Step updateStep(UUID stepId, Quit_Plan_Step step) {
        // Tìm Step hiện tại và Plan liên quan
        Quit_Plan_Step existing = stepRepo.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));
        Quit_Plan plan = existing.getPlan();

        // Xác thực ngày mới
        LocalDate start = step.getStepStartDate();
        LocalDate end = step.getStepEndDate();
        if (start.isBefore(plan.getStartDate()) || end.isAfter(plan.getTargetDate())) {
            throw new IllegalArgumentException("Step dates must lie within plan start and target dates");
        }

        // Cập nhật các trường thông tin
        existing.setStepNumber(step.getStepNumber());
        existing.setStepStartDate(start);
        existing.setStepEndDate(end);
        existing.setTargetCigarettesPerDay(step.getTargetCigarettesPerDay());
        existing.setStepDescription(step.getStepDescription());
        existing.setStepStatus(step.getStepStatus());

        // Lưu và điều chỉnh Plan
        Quit_Plan_Step updated = stepRepo.save(existing);
        adjustPlanBounds(plan);
        return updated;
    }

    @Override
    @Transactional
    public void deleteStep(UUID stepId) {
        // Tìm Step cần xóa và Plan liên quan
        Quit_Plan_Step existing = stepRepo.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));
        Quit_Plan plan = existing.getPlan();

        // Xóa và điều chỉnh lại Plan
        stepRepo.delete(existing);
        adjustPlanBounds(plan);
    }

    @Override
    public List<Quit_Plan_Step> getStepsByPlanOrderByNumber(UUID planId) {
        // Trả về danh sách bước, đã sắp xếp theo stepNumber
        return stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
    }

    /**
     * Phương thức tự động điều chỉnh startDate và targetDate của Quit Plan
     * dựa trên phần tử đầu và phần tử cuối trong danh sách steps.
     */
    private void adjustPlanBounds(Quit_Plan plan) {
        List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepStartDateAsc(plan.getId());
        if (!steps.isEmpty()) {
            LocalDate newStart = steps.get(0).getStepStartDate(); // ngày bắt đầu của step đầu tiên
            LocalDate newTarget = steps.get(steps.size()-1).getStepEndDate(); // ngày kết thúc của step cuối cùng
            plan.setStartDate(newStart);
            plan.setTargetDate(newTarget);
            planRepo.save(plan); // lưu lại Plan sau khi điều chỉnh
        }
    }
}
