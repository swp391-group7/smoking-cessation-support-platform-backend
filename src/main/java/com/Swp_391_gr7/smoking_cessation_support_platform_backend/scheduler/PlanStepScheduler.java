package com.Swp_391_gr7.smoking_cessation_support_platform_backend.scheduler;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PlanStepScheduler {
    private final QuitPlanStepRepository stepRepo;

    /**
     * Runs every day at midnight to update step statuses based on dates:
     * - Before startDate: "notyet"
     * - Between startDate and endDate (inclusive): "active"
     * - After endDate: "end"
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateStepStatuses() {
        LocalDate today = LocalDate.now();
        List<Quit_Plan_Step> allSteps = stepRepo.findAll();
        for (Quit_Plan_Step step : allSteps) {
            String newStatus;
            if (today.isBefore(step.getStepStartDate())) {
                newStatus = "notyet";
            } else if (!today.isAfter(step.getStepEndDate())) {
                newStatus = "active";
            } else {
                newStatus = "end";
            }

            if (!newStatus.equalsIgnoreCase(step.getStepStatus())) {
                log.info("Updating step {} status from {} to {}", step.getId(), step.getStepStatus(), newStatus);
                step.setStepStatus(newStatus);
                stepRepo.save(step);
            }
        }
    }
}
