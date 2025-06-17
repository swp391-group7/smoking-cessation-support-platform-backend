package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.badge;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeCreationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;

import java.util.List;
import java.util.UUID;

public interface BadgesService {
    BadgeDto create(BadgeCreationRequest dto);
    List<BadgeDto> getAll();
    BadgeDto getById(UUID id);
    void delete(UUID id);
}