package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userbadge;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeDto;

import java.util.List;
import java.util.UUID;

public interface UserBadgeService {
    UserBadgeDto assignBadge(UUID userId, UUID badgeId);
    List<UserBadgeDto> getUserBadges(UUID userId);
}