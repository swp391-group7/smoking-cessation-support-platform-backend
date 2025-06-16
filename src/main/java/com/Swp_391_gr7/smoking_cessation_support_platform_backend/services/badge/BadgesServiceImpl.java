package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.badge;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeCreationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Badges;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.BadgesRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BadgesServiceImpl implements BadgesService {
    private final BadgesRepository badgesRepository;
    private final UserRepository userRepository;

    @Override
    public BadgeDto create(BadgeCreationRequest dto) {
        Badges badge = Badges.builder()
                .badgeName(dto.getBadgeName())
                .badgeDescription(dto.getBadgeDescription())
                .badgeImageUrl(dto.getBadgeImageUrl())
                .build();
        Badges saved = badgesRepository.save(badge);
        return toDto(saved);
    }

    @Override
    public List<BadgeDto> getAll() {
        return badgesRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BadgeDto getById(UUID id) {
        return badgesRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public void delete(UUID id) {
        badgesRepository.deleteById(id);
    }

    private BadgeDto toDto(Badges badge) {
        return BadgeDto.builder()
                .id(badge.getId())
                .badgeName(badge.getBadgeName())
                .badgeDescription(badge.getBadgeDescription())
                .badgeImageUrl(badge.getBadgeImageUrl())
                .createdAt(badge.getCreatedAt())
                .build();
    }
}