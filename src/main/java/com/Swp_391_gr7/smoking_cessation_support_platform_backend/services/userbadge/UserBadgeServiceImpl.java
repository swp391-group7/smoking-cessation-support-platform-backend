package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userbadge;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeCreationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Badges;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.UserBadge;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.BadgesRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserBadgeRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBadgeServiceImpl implements UserBadgeService {
    private final UserBadgeRepository userBadgesRepository;
    private final UserRepository userRepository;
    private final BadgesRepository badgesRepository;

    @Override
    public UserBadgeDto assignBadge(UUID userId, UUID badgeId) {
        // Kiểm tra nếu đã có user-badge rồi thì bỏ qua
        if (userBadgesRepository.existsByUserIdAndBadgeId(userId, badgeId)) {
            return null; // hoặc có thể return Optional.empty() nếu bạn muốn rõ ràng
        }

        // Nếu chưa có thì mới cấp badge
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
        Badges badge = badgesRepository.findById(badgeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Badge not found: " + badgeId));

        UserBadge userBadge = UserBadge.builder().user(user).badge(badge).build();
        UserBadge saved = userBadgesRepository.save(userBadge);
        return toDto(saved);
    }


    @Override
    public List<UserBadgeDto> getUserBadges(UUID userId) {
        return userBadgesRepository.findByUserId(userId).stream().map(this::toDto).collect(Collectors.toList());
    }

    private UserBadgeDto toDto(UserBadge userBadge) {
        BadgeDto badgeDto = BadgeDto.builder()
                .id(userBadge.getBadge().getId())
                .badgeName(userBadge.getBadge().getBadgeName())
                .badgeDescription(userBadge.getBadge().getBadgeDescription())
                .badgeImageUrl(userBadge.getBadge().getBadgeImageUrl())
                .createdAt(userBadge.getBadge().getCreatedAt())
                .build();
        return UserBadgeDto.builder()
                .id(userBadge.getId())
                .userId(userBadge.getUser().getId())
                .badge(badgeDto)
                .achievedAt(userBadge.getAchievedAt())
                .build();
    }
}