package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Blog_Post;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ProgressNotification;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ProgressNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressNotificationServiceImpl implements ProgressNotificationService {
    private final ProgressNotificationRepository progressNotificationRepository;

    @Override
    public ProgressNotificationDto create(CreateProgressNotificationReq req) {
        ProgressNotification entity = ProgressNotification.builder()
                .planId(req.getPlanId())
                .message(req.getMessage())
                .channel(req.getChannel())
                .type(req.getType())
                .isRead(false)
                .build();
        ProgressNotification saved = progressNotificationRepository.save(entity);
        return mapToDto(saved);

        //Blog_Post saved = blogRepository.save(entity);
        //return mapToDto(saved);
    }

    @Override
    public ProgressNotificationDto update(UUID id, UpdateProgressNotificationRequest req) {
        ProgressNotification entity = progressNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
        entity.setMessage(req.getMessage());
        entity.setChannel(req.getChannel());
        entity.setType(req.getType());
        ProgressNotification updated = progressNotificationRepository.save(entity);
        return mapToDto(entity);
    }

    @Override
    public void delete(UUID id) {
        progressNotificationRepository.deleteById(id);
    }

    @Override
    public ProgressNotificationDto changeStatus(UUID id) {
        ProgressNotification entity = progressNotificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
        entity.setIsRead(true);
        ProgressNotification updated = progressNotificationRepository.save(entity);
        return mapToDto(entity);
    }

    @Override
    public ProgressNotificationDto getById(UUID id) {
        return progressNotificationRepository.findById(id).map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("ProgressNotification not found"));
    }

    @Override
    public List<ProgressNotificationDto> getByPlanId(UUID planId) {
        return progressNotificationRepository.findByPlanId(planId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private ProgressNotificationDto mapToDto(ProgressNotification entity) {
        return ProgressNotificationDto.builder()
                .id(entity.getId())
                .planId(entity.getPlanId())
                .message(entity.getMessage())
                .channel(entity.getChannel())
                .type(entity.getType())
                .sentAt(entity.getSentAt())
                .expireAt(entity.getExpirationAt())
                .isRead(entity.getIsRead())
                .build();
    }
}