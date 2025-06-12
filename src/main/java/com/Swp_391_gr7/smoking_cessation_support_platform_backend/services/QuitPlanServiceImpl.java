// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/QuitPlanServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Smoke_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SmokeSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuitPlanServiceImpl implements QuitPlanService {
    private final QuitPlanRepository quitPlanRepository;
    private final UserRepository userRepository;
    private final SmokeSurveyRepository smokeSurveyRepository;

    @Override
    public QuitPlanDto create(UUID userId, QuitPlanCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Smoke_Survey smokeSurvey = smokeSurveyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must complete smoke survey first"));
        Quit_Plan plan = Quit_Plan.builder()
                .user(user)
                .startDate(request.getStartDate())
                .targetDate(request.getTargetDate())
                .method(request.getMethod())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();
        Quit_Plan saved = quitPlanRepository.save(plan);
        return mapToDto(saved);
    }

    @Override
    public QuitPlanDto update(UUID id, QuitPlanCreateRequest request) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        plan.setStartDate(request.getStartDate());
        plan.setTargetDate(request.getTargetDate());
        plan.setMethod(request.getMethod());
        Quit_Plan updated = quitPlanRepository.save(plan);
        return mapToDto(updated);
    }

    @Override
    public QuitPlanDto get(UUID id) {
        Quit_Plan plan = quitPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plan not found"));
        return mapToDto(plan);
    }

    @Override
    public void delete(UUID id) {
        quitPlanRepository.deleteById(id);
    }

    @Override
    public List<QuitPlanResponse> getUserPlans(UUID userId) {
        return quitPlanRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private QuitPlanResponse toResponse(Quit_Plan plan) {
        QuitPlanResponse dto = new QuitPlanResponse();
        dto.setId(plan.getId());
        dto.setUserId(plan.getUser().getId());
        dto.setStartDate(plan.getStartDate());
        dto.setTargetDate(plan.getTargetDate());
        dto.setMethod(plan.getMethod());
        return dto;
    }

    private QuitPlanDto mapToDto(Quit_Plan entity) {
        return QuitPlanDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .coachId(entity.getCoach().getUserId())
                .startDate(entity.getStartDate())
                .smokeSurveyId(entity.getSmokeSurvey().getId())
                .method(entity.getMethod())
                .targetDate(entity.getTargetDate())
                .createAt(entity.getCreateAt())
                .status(entity.getStatus())
                .build();
    }

}