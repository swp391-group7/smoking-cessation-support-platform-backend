// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/quitPlan/QuitPlanServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanWithStepsDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.UpdateQuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.QuitPlanStepDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.UpdatePLanStepRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UserSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage.MembershipPackageService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep.QuitPlanStepService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey.SurveyService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey.UseSurveyService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey.UserSurveyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuitPlanServiceImpl implements QuitPlanService {
    private final QuitPlanRepository quitPlanRepository;
    private final MembershipPackageService membershipService;
    private final UserSurveyServiceImpl userSurveyService;
    private final UserRepository userRepository;
    private final QuitPlanStepService stepService;
    private final UserSurveyRepository repository;
    private final UseSurveyService useSurveyService;
    @Override
    public QuitPlanDto create(UUID userId, QuitPlanCreateRequest request) {
        // Close any existing active plans
        quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "active")
                .forEach(p -> {
                    p.setStatus("completed");
                    quitPlanRepository.save(p);
                });
        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        // Create new plan with startDate = now
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
                .startDate(LocalDate.now())    // auto-set to today
                .targetDate(request.getTargetDate())
                .method(request.getMethod())
                .status(request.getStatus())
                .maxZeroStreak(0)
                .currentZeroStreak(0)
                .build();
        Quit_Plan saved = quitPlanRepository.save(plan);
        return mapToDto(saved);
    }

//    @Override
//    public QuitPlanDto update(UUID id, QuitPlanCreateRequest request, UUID userId) {
//        Quit_Plan plan = quitPlanRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
//        if (!plan.getUser().getId().equals(userId)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to edit this plan");
//        }
//        plan.setStartDate(request.getStartDate());
//        plan.setTargetDate(request.getTargetDate());
//        plan.setMethod(request.getMethod());
//        plan.setStatus(request.getStatus());
//        Quit_Plan updated = quitPlanRepository.save(plan);
//        return mapToDto(updated);
//    }

    @Override
    public QuitPlanDto getById(UUID id) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        return mapToDto(plan);
    }

    @Override
    public void delete(UUID id, UUID userId) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        if (!plan.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this plan");
        }
        quitPlanRepository.delete(plan);
    }

    @Override
    public List<QuitPlanDto> getAll() {
        return quitPlanRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuitPlanDto> searchByMethodOrStatus(String method, String status, UUID userId) {
        List<Quit_Plan> plans;
        if (method != null && !method.isEmpty()) {
            plans = quitPlanRepository.findByMethodContainingIgnoreCaseAndUserId(method, userId);
        } else if (status != null && !status.isEmpty()) {
            plans = quitPlanRepository.findByStatusContainingIgnoreCaseAndUserId(status, userId);
        } else {
            plans = List.of();
        }
        return plans.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuitPlanWithStepsDto generatePlanFromSurvey(UUID userId, UUID surveyId) {
        // 1. Check if user has an active package
        if (!membershipService.hasActivePackageByUser(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have an active package");
        }

        // 2. Get survey by surveyId
        UserSurveyDto survey = userSurveyService.getSurveyById(surveyId);
        int cigarettesPerDay = survey.getCigarettesPerDay();
        int dependency = survey.getDependencyLevel();

        // 3. Calculate start and target date
        LocalDate startDate = LocalDate.now();
        int planWeeks = Math.max(dependency * 2, 4);
        LocalDate targetDate = startDate.plusWeeks(planWeeks);

        // 4. Log plan details
        System.out.println("[PLAN] start=" + startDate + ", target=" + targetDate);

        // 5. Create draft plan
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User_Survey user_survey = repository.findFirstByUserIdOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No survey found for userId: " + userId));
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
                .user_survey(user_survey)
                .startDate(startDate)
                .targetDate(targetDate)
                .method("GRADUAL")
                .status("draft")
                .currentZeroStreak(0)
                .maxZeroStreak(0)
                .build();
        Quit_Plan savedPlan = quitPlanRepository.save(plan);

        // 6. Generate reduction steps and log details
        generateQuitSteps(savedPlan, cigarettesPerDay, dependency, startDate, targetDate);

        // 7. Return DTO with steps
        return mapToDtoWithSteps(savedPlan);
    }

    private void generateQuitSteps(Quit_Plan plan, int cigarettesPerDay, int dependency,
                                   LocalDate startDate, LocalDate targetDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, targetDate) + 1;
        int numSteps = calculateOptimalSteps(cigarettesPerDay, dependency);

        // Ensure numSteps doesn't exceed totalDays
        if (numSteps > totalDays) {
            numSteps = (int) totalDays;
        }

        // Ensure each step has at least 1 day
        long minDays = 1;
        if (totalDays / numSteps < minDays) {
            numSteps = (int) totalDays;
        }

        long baseDays = totalDays / numSteps;
        long extraDays = totalDays % numSteps;
        LocalDate cursor = startDate;

        System.out.println("[DEBUG] totalDays=" + totalDays + ", numSteps=" + numSteps
                + ", baseDays=" + baseDays + ", extraDays=" + extraDays);

        for (int i = 0; i < numSteps; i++) {
            // Check if cursor doesn't exceed targetDate
            if (cursor.isAfter(targetDate)) {
                System.out.println("[WARNING] Cursor exceeds targetDate, stopping step creation");
                break;
            }

            long days = baseDays + (i < extraDays ? 1 : 0);
            days = Math.max(days, 1); // Ensure at least 1 day

            LocalDate stepStart = cursor;
            LocalDate stepEnd;

            if (i == numSteps - 1) {
                // Last step always ends on targetDate
                stepEnd = targetDate;
            } else {
                stepEnd = stepStart.plusDays(days - 1);
                // Ensure stepEnd doesn't exceed targetDate
                if (stepEnd.isAfter(targetDate)) {
                    stepEnd = targetDate;
                }
            }

            System.out.println("[DEBUG] Step " + (i+1) + ": " + stepStart + " -> " + stepEnd);

            // Additional validation
            if (!isValidStepDates(stepStart, stepEnd, startDate, targetDate)) {
                System.out.println("[ERROR] Invalid step dates: " + stepStart + " -> " + stepEnd);
                throw new IllegalArgumentException("Invalid step dates calculated");
            }

            Quit_Plan_Step step = Quit_Plan_Step.builder()
                    .plan(plan)
                    .stepNumber(i+1)
                    .stepStartDate(stepStart)
                    .stepEndDate(stepEnd)
                    .targetCigarettesPerDay(calculateTargetCigarettes(cigarettesPerDay, i, numSteps))
                    .stepStatus("draft")
                    .stepDescription(createStepDescription(calculateTargetCigarettes(cigarettesPerDay, i, numSteps), i+1, numSteps))
                    .build();

            try {
                stepService.createStep(plan.getId(), step);
            } catch (IllegalArgumentException e) {
                System.out.println("[ERROR] Failed to create step " + (i+1) + ": " + e.getMessage());
                System.out.println("[ERROR] Step details: start=" + stepStart + ", end=" + stepEnd +
                        ", planStart=" + startDate + ", planEnd=" + targetDate);
                throw e;
            }

            // Update cursor for next step
            cursor = stepEnd.plusDays(1);

            // If cursor already exceeds targetDate, stop
            if (cursor.isAfter(targetDate)) {
                System.out.println("[INFO] All steps created successfully, cursor=" + cursor);
                break;
            }
        }
    }

    private QuitPlanWithStepsDto mapToDtoWithSteps(Quit_Plan plan) {
        List<Quit_Plan_Step> steps = stepService.getStepsByPlan(plan.getId());

        // Convert entities to DTOs
        List<QuitPlanStepDto> stepDtos = steps.stream()
                .map(this::mapStepToDto)
                .collect(Collectors.toList());

        return QuitPlanWithStepsDto.builder()
                .id(plan.getId())
                .userId(plan.getUser().getId())
                .userSurveyId(plan.getUser_survey().getId())
                .startDate(plan.getStartDate())
                .targetDate(plan.getTargetDate())
                .method(plan.getMethod())
                .status(plan.getStatus())
                .currentZeroStreak(plan.getCurrentZeroStreak())
                .maxZeroStreak(plan.getMaxZeroStreak())
                .createAt(plan.getCreateAt())
                .steps(stepDtos) // Use DTO list
                .build();
    }

    // Helper method to convert Step entity to DTO
    private QuitPlanStepDto mapStepToDto(Quit_Plan_Step step) {
        return QuitPlanStepDto.builder()
                .id(step.getId())
                .quitPlanId(step.getPlan().getId())
                .stepNumber(step.getStepNumber())
                .stepStartDate(step.getStepStartDate())
                .stepEndDate(step.getStepEndDate())
                .targetCigarettesPerDay(step.getTargetCigarettesPerDay())
                .stepDescription(step.getStepDescription())
                .status(step.getStepStatus())
                .createAt(step.getCreateAt())
                .build();
    }

    /**
     * Validate if step dates are within plan boundaries
     */
    private boolean isValidStepDates(LocalDate stepStart, LocalDate stepEnd,
                                     LocalDate planStart, LocalDate planEnd) {
        if (stepStart == null || stepEnd == null || planStart == null || planEnd == null) {
            System.out.println("[VALIDATION] Null date detected");
            return false;
        }

        boolean isValid = !stepStart.isBefore(planStart) &&
                !stepEnd.isAfter(planEnd) &&
                !stepStart.isAfter(stepEnd);

        if (!isValid) {
            System.out.println("[VALIDATION] Invalid dates: stepStart=" + stepStart +
                    ", stepEnd=" + stepEnd + ", planStart=" + planStart +
                    ", planEnd=" + planEnd);
        }

        return isValid;
    }

    private int calculateOptimalSteps(int cigarettesPerDay, int dependency) {
        // Logic to optimize number of steps based on:
        // - Cigarettes per day: more cigarettes = more steps
        // - Dependency level: higher dependency = more steps

        int baseSteps = Math.max(3, cigarettesPerDay / 5); // Minimum 3 steps
        int dependencyBonus = dependency / 2; // Add steps based on dependency

        return Math.min(baseSteps + dependencyBonus, 10); // Maximum 10 steps
    }

    private int calculateTargetCigarettes(int initialCigarettes, int stepIndex, int totalSteps) {
        // Gradual reduction algorithm: start from 80% and decrease to 0
        if (stepIndex == totalSteps - 1) {
            return 0; // Last step is always 0
        }

        // Reduce using logarithmic function to create natural reduction curve
        double progress = (double) (stepIndex + 1) / totalSteps;
        double reductionFactor = 1 - Math.pow(progress, 1.5);

        int target = (int) Math.round(initialCigarettes * reductionFactor);

        // Ensure at least 1 cigarette reduction per step
        int maxAllowed = initialCigarettes - stepIndex - 1;
        return Math.max(0, Math.min(target, maxAllowed));
    }

    private String createStepDescription(int targetCigarettes, int stepNumber, int totalSteps) {
        if (targetCigarettes == 0) {
            return "Step " + stepNumber + "/" + totalSteps + ": Quit smoking completely";
        }

        String motivation = "";
        if (stepNumber == 1) {
            motivation = " - Start your journey!";
        } else if (stepNumber == totalSteps - 1) {
            motivation = " - Almost there!";
        } else if (stepNumber > totalSteps / 2) {
            motivation = " - Halfway through!";
        }

        return "Step " + stepNumber + "/" + totalSteps + ": Reduce to " +
                targetCigarettes + " cigarettes/day" + motivation;
    }

    @Override
    public QuitPlanDto activatePlan(UUID userId, UUID planId) {
        // 1. Complete old active plan
        quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "active")
                .forEach(oldPlan -> {
                    oldPlan.setStatus("completed");
                    quitPlanRepository.save(oldPlan);
                    // Update old steps to completed
                    stepService.getStepsByPlan(oldPlan.getId())
                            .forEach(s -> {
                                stepService.updateStepByNumber(
                                        oldPlan.getId(),
                                        s.getStepNumber(),
                                        UpdatePLanStepRequest.builder()
                                                .stepStartDate(s.getStepStartDate())
                                                .stepEndDate(s.getStepEndDate())
                                                .targetCigarettesPerDay(s.getTargetCigarettesPerDay())
                                                .stepDescription(s.getStepDescription())
                                                .build()
                                );

                            });
                });

        // 2. Activate new plan
        Quit_Plan plan = quitPlanRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Plan not found"));
        if (!"draft".equalsIgnoreCase(plan.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only draft plans can be activated");
        }
        plan.setStatus("active");
        quitPlanRepository.save(plan);

        // 3. Update all steps of the new plan according to computeStatus()
        stepService.getStepsByPlan(planId).forEach(s -> {
            stepService.updateStepByNumber(
                    planId,
                    s.getStepNumber(),
                    UpdatePLanStepRequest.builder()
                            .stepStartDate(s.getStepStartDate())
                            .stepEndDate(s.getStepEndDate())
                            .targetCigarettesPerDay(s.getTargetCigarettesPerDay())
                            .stepDescription(s.getStepDescription())
                            .build()
            );

        });

        return mapToDto(plan);
    }


    @Override
    public QuitPlanDto getActivePlanByUserId(UUID userId) {
        Quit_Plan plan = quitPlanRepository.findFirstByUserIdAndStatusIgnoreCase(userId, "active");
        if (plan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No active plan found for this user");
        }
        return mapToDto(plan);
    }

    @Override
    public QuitPlanDto createImmediatePlan(UUID userId) {
        // Close existing active plans
        quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "active").forEach(p -> {
            p.setStatus("completed");
            quitPlanRepository.save(p);
        });
        // Create immediate plan
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User_Survey user_survey = repository.findFirstByUserIdOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No survey found for userId: " + userId));
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
                .user_survey(user_survey)
                .startDate(LocalDate.now())
                .targetDate(LocalDate.now())
                .method("IMMEDIATE")
                .status("active")
                .build();
        Quit_Plan saved = quitPlanRepository.save(plan);
        return mapToDto(saved);
    }

    @Override
    public QuitPlanDto createDraftPlan(UUID userId) {
        // Xóa toàn bộ draft cũ
        List<Quit_Plan> oldDrafts = quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "draft");
        if (!oldDrafts.isEmpty()) {
            quitPlanRepository.deleteAll(oldDrafts);
        }
        // Tạo draft mới
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        User_Survey user_survey = repository.findFirstByUserIdOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No survey found for userId: " + userId));
        Quit_Plan draft = Quit_Plan.builder()
                .user(user)
                .user_survey(user_survey)
                .startDate(LocalDate.now())
                .targetDate(LocalDate.now())
                .method("GRADUAL")
                .status("draft")
                .currentZeroStreak(0)
                .maxZeroStreak(0)
                .build();
        Quit_Plan saved = quitPlanRepository.save(draft);
        return mapToDto(saved);
    }

    @Override
    public void deleteAllDrafts(UUID userId) {
        List<Quit_Plan> drafts = quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "draft");
        quitPlanRepository.deleteAll(drafts);
    }

    @Override
    public QuitPlanDto updateLatestDraft(UUID userId, UpdateQuitPlanRequest request) {
        // 1. Đóng tất cả plan đang active hiện có
        List<Quit_Plan> activePlans = quitPlanRepository
                .findByUserIdAndStatusIgnoreCase(userId, "active");
        if (!activePlans.isEmpty()) {
            for (Quit_Plan p : activePlans) {
                p.setStatus("completed");
            }
            // lưu batch để tránh gọi save nhiều lần
            quitPlanRepository.saveAll(activePlans);
        }

        // 2. Tìm draft mới nhất
        Quit_Plan latest = quitPlanRepository
                .findByUserIdAndStatusIgnoreCase(userId, "draft").stream()
                .max(Comparator.comparing(Quit_Plan::getCreateAt))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No draft found"));

        // 3. Chuyển draft đó thành active
        latest.setTargetDate(request.getTargetDate());
        latest.setStatus("active");
        Quit_Plan updated = quitPlanRepository.save(latest);

        return mapToDto(updated);
    }


    private QuitPlanDto mapToDto(Quit_Plan entity) {
        return QuitPlanDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .userSurveyId(entity.getUser_survey().getId())
                .startDate(entity.getStartDate())
                .targetDate(entity.getTargetDate())
                .method(entity.getMethod())
                .currentZeroStreak(entity.getCurrentZeroStreak())
                .maxZeroStreak(entity.getMaxZeroStreak())
                .status(entity.getStatus())
                .createAt(entity.getCreateAt())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public Integer getCurrentZeroStreak(UUID userId) {
        Quit_Plan plan = quitPlanRepository
                .findFirstByUserIdAndStatusIgnoreCase(userId, "active");
        if (plan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No active plan found for user");
        }
        return plan.getCurrentZeroStreak();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getMaxZeroStreak(UUID userId) {
        Quit_Plan plan = quitPlanRepository
                .findFirstByUserIdAndStatusIgnoreCase(userId, "active");
        if (plan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No active plan found for user");
        }
        return plan.getMaxZeroStreak();
    }


}
