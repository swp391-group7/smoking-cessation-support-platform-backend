package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cessationprogress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cessationprogress.CessationProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CessationProgressService {
    CessationProgressDto create(Cessation_Progress progress);
    List<CessationProgressDto> findAll();
    Optional<CessationProgressDto> findById(UUID id);
    CessationProgressDto update(UUID id, Cessation_Progress progress);
    List<CessationProgressDto> findByUserId(UUID userId);
    void delete(UUID id);
}
