package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.usernotification;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.notification.NotificationCreationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.notification.NotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.notification.NotificationUpdateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Notification;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    private NotificationDto mapToDto(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .title(n.getTitle())
                .message(n.getMessage())
                .channel(n.getChannel())
                .type(n.getType())
                .sentAt(n.getSentAt())
                .expireAt(n.getExpiredAt())
                .build();
    }

    private void updateEntity(Notification n, NotificationUpdateRequest dto) {
        n.setTitle(dto.getTitle());
        n.setMessage(dto.getMessage());
        n.setChannel(dto.getChannel());
        n.setType(dto.getType());
    }

    @Override
    public NotificationDto create(UUID userId, NotificationCreationRequest req) {
        Notification n = Notification.builder()
                .userId(userId)
                .title(req.getTitle())
                .message(req.getMessage())
                .channel(req.getChannel())
                .type(req.getType())
                .build();
        Notification saved = notificationRepository.save(n);
        return mapToDto(saved);
    }

    @Override
    public NotificationDto update(UUID id, NotificationUpdateRequest dto) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        updateEntity(n, dto);
        Notification updated = notificationRepository.save(n);
        return mapToDto(updated);
    }

    @Override
    public void delete(UUID id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public NotificationDto getById(UUID id) {
        return notificationRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }

    @Override
    public List<NotificationDto> getByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // New methods for search by title, content, type (case-insensitive, contains)
    public List<NotificationDto> searchByTitle(String title) {
        return notificationRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> searchByContent(String content) {
        return notificationRepository.findByMessageContainingIgnoreCase(content).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<NotificationDto> searchByType(String type) {
        return notificationRepository.findByTypeContainingIgnoreCase(type).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}