package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QuitPlanRepository extends JpaRepository<Quit_Plan, UUID> {
    List<Quit_Plan> findByUserId(UUID userId);
    Optional<Quit_Plan> findByUserIdAndStatus(UUID userId, String status);
    List<Quit_Plan> findByStatus(String status);
}
