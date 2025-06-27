// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/quitPlan/QuitPlanServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.UpdateQuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuitPlanServiceImpl implements QuitPlanService {
    private final QuitPlanRepository quitPlanRepository;
    private final UserRepository userRepository;

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
    public QuitPlanDto generatePlanFromSurvey(UUID userId, UUID smokeSurveyId) {
        throw new UnsupportedOperationException("Method not implemented yet");
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
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
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
        Quit_Plan draft = Quit_Plan.builder()
                .user(user)
                .startDate(LocalDate.now())
                .targetDate(LocalDate.now())
                .method("GRADUAL")
                .status("draft")
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
        Quit_Plan latest = quitPlanRepository.findByUserIdAndStatusIgnoreCase(userId, "draft").stream()
                .max(Comparator.comparing(Quit_Plan::getCreateAt))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No draft found"));
        latest.setTargetDate(request.getTargetDate());
        latest.setStatus("active");
        Quit_Plan updated = quitPlanRepository.save(latest);
        return mapToDto(updated);
    }

    private QuitPlanDto mapToDto(Quit_Plan entity) {
        return QuitPlanDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .startDate(entity.getStartDate())
                .targetDate(entity.getTargetDate())
                .method(entity.getMethod())
                .status(entity.getStatus())
                .createAt(entity.getCreateAt())
                .build();
    }
}
