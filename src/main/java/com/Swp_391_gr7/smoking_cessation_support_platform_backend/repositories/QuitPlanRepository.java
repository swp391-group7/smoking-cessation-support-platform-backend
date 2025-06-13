package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuitPlanRepository extends JpaRepository<Quit_Plan, UUID> {
    List<Quit_Plan> findByUserId(UUID userId);
    List<Quit_Plan> findByMethodContainingIgnoreCaseAndUserId(String method, UUID userId);
    List<Quit_Plan> findByStatusContainingIgnoreCaseAndUserId(String status, UUID userId);
    List<Quit_Plan> findBySmokeSurveyId(UUID smokeSurveyId);
}