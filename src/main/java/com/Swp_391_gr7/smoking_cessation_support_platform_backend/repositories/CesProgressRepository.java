package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CesProgressRepository extends JpaRepository<Cessation_Progress, UUID> {
    // 1. Lấy tất cả progress theo plan ID
    List<Cessation_Progress> findByPlan_Id(UUID planId);

    @Query("SELECT cp FROM Cessation_Progress cp WHERE cp.planStep.stepNumber = :stepNumber")
    List<Cessation_Progress> findByPlanStepNumber(@Param("stepNumber") Integer stepNumber);

    // 4. Lấy tất cả progress trong một ngày nhất định (logDate)
    List<Cessation_Progress> findByLogDate(LocalDate logDate);
    List<Cessation_Progress> findByPlanIdOrderByLogDateDesc(UUID planId);
}
