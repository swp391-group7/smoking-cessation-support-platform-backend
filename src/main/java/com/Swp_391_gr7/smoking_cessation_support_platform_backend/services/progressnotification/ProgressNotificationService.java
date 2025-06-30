package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ProgressNotification;

import java.util.List;
import java.util.UUID;

public interface ProgressNotificationService {
    ProgressNotificationDto create(CreateProgressNotificationReq req);
    ProgressNotificationDto update(UUID id, UpdateProgressNotificationRequest dto);
    void delete(UUID id);
    ProgressNotificationDto changeStatus(UUID id);
    ProgressNotificationDto getById(UUID id);
    List<ProgressNotificationDto> getByPlanId(UUID planId);
}