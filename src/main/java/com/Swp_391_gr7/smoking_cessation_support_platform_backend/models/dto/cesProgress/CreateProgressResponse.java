package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateProgressResponse {
    private CesProgressDto progress;
    private List<BadgeDetailDto> newBadges;
}

