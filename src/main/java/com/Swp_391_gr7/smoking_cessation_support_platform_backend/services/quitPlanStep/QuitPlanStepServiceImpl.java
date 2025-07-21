// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/quitPlanStep/QuitPlanStepServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.UpdatePLanStepRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuitPlanStepServiceImpl implements QuitPlanStepService {
    private final QuitPlanStepRepository stepRepo;
    private final QuitPlanRepository planRepo;

    @Override
    public List<Quit_Plan_Step> getStepsByPlan(UUID planId) {
        return stepRepo.findByPlanIdOrderByStepStartDateAsc(planId);
    }

    @Override
    public List<Quit_Plan_Step> getStepsByPlanOrderByNumber(UUID planId) {
        return stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
    }

    @Override
    public Quit_Plan_Step createStep(UUID planId, Quit_Plan_Step step) {
        Quit_Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
        LocalDate start = step.getStepStartDate();
        LocalDate end = step.getStepEndDate();
        if (start.isBefore(plan.getStartDate()) || end.isAfter(plan.getTargetDate())) {
            throw new IllegalArgumentException("Step dates must lie within plan dates");
        }
        List<Quit_Plan_Step> existing = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        int next = existing.stream().map(Quit_Plan_Step::getStepNumber).max(Comparator.naturalOrder()).map(n->n+1).orElse(1);
        step.setStepNumber(next);
        step.setPlan(plan);
        if (step.getStepStatus() == null) {
            step.setStepStatus(computeStatus(start, end));
        }
        Quit_Plan_Step saved = stepRepo.save(step);
        adjustPlanBounds(plan);
        return saved;
    }

    @Override
    public Quit_Plan_Step createDefaultStep(UUID planId) {
        Quit_Plan plan = planRepo.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found: " + planId));
        List<Quit_Plan_Step> existing = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        int next = existing.stream().map(Quit_Plan_Step::getStepNumber).max(Comparator.naturalOrder()).map(n->n+1).orElse(1);
        Quit_Plan_Step step = Quit_Plan_Step.builder()
                .stepNumber(next)
                .stepStartDate(plan.getStartDate())
                .stepEndDate(plan.getTargetDate())
                .targetCigarettesPerDay(0)
                .stepDescription("")
                .stepStatus("draft") // Mặc định là draft
                .plan(plan)
                .build();
        return createStep(planId, step);

    }

    @Override
    public Quit_Plan_Step updateStep(UUID stepId, Quit_Plan_Step step) {
        Quit_Plan_Step existing = stepRepo.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));
        Quit_Plan plan = existing.getPlan();
        LocalDate start = step.getStepStartDate();
        LocalDate end = step.getStepEndDate();
        if (start.isBefore(plan.getStartDate()) || end.isAfter(plan.getTargetDate())) {
            throw new IllegalArgumentException("Step dates must lie within plan dates");
        }
        existing.setStepNumber(step.getStepNumber());
        existing.setStepStartDate(start);
        existing.setStepEndDate(end);
// ** Tính lại status, không lấy của client **
        existing.setStepStatus(computeStatus(start, end));
        existing.setTargetCigarettesPerDay(step.getTargetCigarettesPerDay());
        existing.setStepDescription(step.getStepDescription());
        Quit_Plan_Step updated = stepRepo.save(existing);
        adjustPlanBounds(plan);
        return updated;

    }

    @Override
    public Quit_Plan_Step updateStepByNumber(UUID planId, Integer stepNumber, UpdatePLanStepRequest req) {
        List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        Quit_Plan_Step target = steps.stream().filter(s -> s.getStepNumber().equals(stepNumber)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Step not found: number=" + stepNumber));
        LocalDate s = req.getStepStartDate();
        LocalDate e = req.getStepEndDate();
        target.setStepStartDate(s);
        target.setStepEndDate(e);
        target.setTargetCigarettesPerDay(req.getTargetCigarettesPerDay());
        target.setStepDescription(req.getStepDescription());
// ** Tính status đúng theo ngày mới **
        target.setStepStatus(computeStatus(s, e));
        Quit_Plan_Step updated = stepRepo.save(target);
        adjustPlanBounds(target.getPlan());
        return updated;

    }

    @Override
    public void deleteStep(UUID stepId) {
        Quit_Plan_Step existing = stepRepo.findById(stepId)
                .orElseThrow(() -> new IllegalArgumentException("Step not found: " + stepId));
        Quit_Plan plan = existing.getPlan();
        stepRepo.delete(existing);
        resetStepNumbers(plan.getId());
        adjustPlanBounds(plan);
    }

    @Override
    public void deleteStepByNumber(UUID planId, Integer stepNumber) {
        List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        Quit_Plan_Step target = steps.stream().filter(s -> s.getStepNumber().equals(stepNumber)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Step not found: number=" + stepNumber));
        Quit_Plan plan = target.getPlan();
        stepRepo.delete(target);
        resetStepNumbers(planId);
        adjustPlanBounds(plan);
    }

    @Override
    public void deleteAllDraftSteps(UUID planId) {
        List<Quit_Plan_Step> drafts = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        drafts.stream().filter(s -> "draft".equalsIgnoreCase(s.getStepStatus()))
                .forEach(stepRepo::delete);
        resetStepNumbers(planId);
    }

    private void resetStepNumbers(UUID planId) {
        List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepNumberAsc(planId);
        int num = 1;
        for (Quit_Plan_Step s : steps) {
            s.setStepNumber(num++);
            stepRepo.save(s);
        }
    }

    private void adjustPlanBounds(Quit_Plan plan) {
        // Chỉ adjust bounds nếu plan không phải là draft được tạo từ survey
        if ("draft".equals(plan.getStatus())) {
            return; // Không adjust bounds cho draft plan
        }

        List<Quit_Plan_Step> steps = stepRepo.findByPlanIdOrderByStepStartDateAsc(plan.getId());
        if (!steps.isEmpty()) {
            plan.setStartDate(steps.get(0).getStepStartDate());
            plan.setTargetDate(steps.get(steps.size() - 1).getStepEndDate());
            planRepo.save(plan);
        }
    }
    private String computeStatus(LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(start)) {
            return "notyet";
        } else if (!today.isAfter(end)) {
            return "active";
        } else {
            return "end";
        }
    }
}
