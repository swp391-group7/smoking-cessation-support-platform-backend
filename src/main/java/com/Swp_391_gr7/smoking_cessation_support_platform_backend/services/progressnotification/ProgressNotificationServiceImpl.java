package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ProgressNotification;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ProgressNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgressNotificationServiceImpl implements ProgressNotificationService {
    private final ProgressNotificationRepository progressNotificationRepository;

    @Override
    public ProgressNotification create(CreateProgressNotificationReq req) {
        ProgressNotification entity = ProgressNotification.builder()
                .planId(req.getPlanId())
                .message(req.getMessage())
                .channel(req.getChannel())
                .type(req.getType())
                .isRead(false)
                .build();
        // Note: title is present in DTO but not in entity, so it's ignored here
        return progressNotificationRepository.save(entity);
    }

    @Override
    public ProgressNotification update(UUID id, UpdateProgressNotificationRequest req) {
        ProgressNotification entity = progressNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
        entity.setMessage(req.getMessage());
        entity.setChannel(req.getChannel());
        entity.setType(req.getType());
        // Note: title is present in DTO but not in entity, so it's ignored here
        return progressNotificationRepository.save(entity);
    }

    @Override
    public void delete(UUID id) {
        progressNotificationRepository.deleteById(id);
    }

    @Override
    public ProgressNotification changeStatus(UUID id, boolean isRead) {
        ProgressNotification entity = progressNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
        entity.setIsRead(isRead);
        return progressNotificationRepository.save(entity);
    }

    @Override
    public ProgressNotification getById(UUID id) {
        return progressNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
    }

    @Override
    public List<ProgressNotification> getByPlanId(UUID planId) {
        return progressNotificationRepository.findByPlanId(planId);
    }
    private ProgressNotificationDto mapToDto(ProgressNotification entity) {
        return ProgressNotificationDto.builder()
                .id(entity.getId())
                .planId(entity.getPlanId())
                .title(null) // Entity does not have title field
                .message(entity.getMessage())
                .channel(entity.getChannel())
                .type(entity.getType())
                .sentAt(entity.getSentAt())
                .expireAt(entity.getExpirationAt())
                .isRead(entity.getIsRead())
                .build();
    }
}