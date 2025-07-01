package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.usernotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.notification.NotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDto create(NotificationDto dto);
    NotificationDto update(UUID id, NotificationDto dto);
    void delete(UUID id);
    NotificationDto getById(UUID id);
    List<NotificationDto> getByUserId(UUID userId);
}