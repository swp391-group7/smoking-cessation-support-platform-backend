package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.coach;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.CoachDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.UpdateCoachRequest;

import java.util.List;
import java.util.UUID;

public interface CoachService {
    CoachDto createCoach(UUID userId, CoachDto dto);
    CoachDto getCoachById(UUID userId);
    CoachDto updateCoach(UUID userId, UpdateCoachRequest request);
    void deleteCoach(UUID userId);
    List<CoachDto> getAllCoaches();

}
