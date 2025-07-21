package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ProgressNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressNotificationRepository extends JpaRepository<ProgressNotification, UUID> {
    List<ProgressNotification> findByPlanId(UUID planId);
    List<ProgressNotification> findByTypeIgnoreCase(String type);
    List<ProgressNotification> findByChannelIgnoreCase(String channel);

    // Gửi bởi ai (senderId)
    List<ProgressNotification> findBySenderIdAndTypeIgnoreCase(UUID senderId, String type);
    // theo type của notification trong 1 plan cụ thể
    List<ProgressNotification> findByPlanIdAndTypeIgnoreCase(UUID planId, String type);

    // theo channel của notification trong 1 plan cụ thể
    List<ProgressNotification> findByPlanIdAndChannelIgnoreCase(UUID planId, String channel);

}
