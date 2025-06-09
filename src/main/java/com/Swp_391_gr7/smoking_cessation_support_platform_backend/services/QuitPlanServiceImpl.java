// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/QuitPlanServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuitPlanServiceImpl implements QuitPlanService {
    private final QuitPlanRepository quitPlanRepository;
    private final UserRepository userRepository;

    @Override
    public QuitPlanResponse create(QuitPlanRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
                .startDate(request.getStartDate())
                .goal(request.getGoal())
                .note(request.getNote())
                .build();
        Quit_Plan saved = quitPlanRepository.save(plan);
        return toResponse(saved);
    }

    @Override
    public QuitPlanResponse update(UUID id, QuitPlanRequest request) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        plan.setStartDate(request.getStartDate());
        plan.setGoal(request.getGoal());
        plan.setNote(request.getNote());
        Quit_Plan updated = quitPlanRepository.save(plan);
        return toResponse(updated);
    }

    @Override
    public QuitPlanResponse get(UUID id) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        return toResponse(plan);
    }

    @Override
    public void delete(UUID id) {
        quitPlanRepository.deleteById(id);
    }

    private QuitPlanResponse toResponse(Quit_Plan plan) {
        QuitPlanResponse dto = new QuitPlanResponse();
        dto.setId(plan.getId());
        dto.setUserId(plan.getUser().getId());
        dto.setStartDate(plan.getStartDate());
        dto.setGoal(plan.getTargetDate());
        dto.setNote(plan.getNote());
        return dto;
    }
}