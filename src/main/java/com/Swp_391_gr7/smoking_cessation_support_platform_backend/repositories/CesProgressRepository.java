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

    interface DailyTotal {
        LocalDate getLogDate();
        Integer getTotalCigarettes();
    }

    // Query tổng số thuốc theo ngày, theo planId
    @Query("SELECT cp.logDate AS logDate, SUM(cp.cigarettesSmoked) AS totalCigarettes " +
            "FROM Cessation_Progress cp " +
            "WHERE cp.plan.id = :planId " +
            "GROUP BY cp.logDate " +
            "ORDER BY cp.logDate DESC")
    List<DailyTotal> findDailyTotalsByPlan(@Param("planId") UUID planId);
    List<Cessation_Progress> findAllByPlanIdOrderByLogDateAsc(UUID planId);
    List<Cessation_Progress> findByPlan_IdAndLogDate(UUID planId, LocalDate logDate);

        /* Đếm số lượng progress records hôm nay theo planId
     */
    @Query("SELECT COUNT(cp) FROM Cessation_Progress cp WHERE cp.plan.id = :planId AND cp.logDate = :date")
    int countByPlanIdAndLogDate(@Param("planId") UUID planId, @Param("date") LocalDate date);

    /**
     * Đếm số lượng progress records hôm nay theo userId (plan active)
     */
    @Query("SELECT COUNT(cp) FROM Cessation_Progress cp " +
            "WHERE cp.plan.user.id = :userId " +
            "AND cp.plan.status = 'active' " +
            "AND cp.logDate = :date")
    int countByUserIdAndLogDate(@Param("userId") UUID userId, @Param("date") LocalDate date);

    List<Cessation_Progress> findByPlanStep_Id(UUID planStepId);
}
