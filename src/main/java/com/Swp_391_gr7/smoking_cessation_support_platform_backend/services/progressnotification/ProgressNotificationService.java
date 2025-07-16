package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;

import java.util.List;
import java.util.UUID;

public interface ProgressNotificationService {

    // Coach gửi remind hoặc chat cho user
    ProgressNotificationDto coachNotify(UUID coachId, UUID planId, CreateProgressNotificationReq req);

    // User gửi chat cho coach
    ProgressNotificationDto userChat(UUID userId, UUID planId, CreateProgressNotificationReq req);

    // Cập nhật message/channel/type
    ProgressNotificationDto update(UUID id, UpdateProgressNotificationRequest req);

    // Đánh dấu đã đọc
    ProgressNotificationDto changeStatus(UUID id);

    // Lấy theo plan
    List<ProgressNotificationDto> getByPlanId(UUID planId);

    // Lọc theo type
    List<ProgressNotificationDto> getByType(String type);

    // Lọc theo channel
    List<ProgressNotificationDto> getByChannel(String channel);

    // Lấy tất cả remind mà coach đã gửi
    List<ProgressNotificationDto> getRemindsByCoach(UUID coachId);
}
