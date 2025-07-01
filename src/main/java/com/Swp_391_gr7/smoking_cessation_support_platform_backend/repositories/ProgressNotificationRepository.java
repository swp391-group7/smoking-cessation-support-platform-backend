package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ProgressNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressNotificationRepository extends JpaRepository<ProgressNotification, UUID> {
    List<ProgressNotification> findByPlanId(UUID planId);

    List<ProgressNotification> findByMessageContainingIgnoreCase(String message);
    List<ProgressNotification> findByTypeContainingIgnoreCase(String type);
}
