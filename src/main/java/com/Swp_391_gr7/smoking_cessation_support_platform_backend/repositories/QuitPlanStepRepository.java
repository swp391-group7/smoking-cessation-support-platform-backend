package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuitPlanStepRepository extends JpaRepository<Quit_Plan_Step, UUID> {
    List<Quit_Plan_Step> findByPlanIdOrderByStepStartDateAsc(UUID planId);
    List<Quit_Plan_Step> findByPlanIdOrderByStepNumberAsc(UUID planId);
}
