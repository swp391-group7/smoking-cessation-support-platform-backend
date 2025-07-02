package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CessationProgressRepository extends JpaRepository<Cessation_Progress, UUID> {
    List<Cessation_Progress> findByUser_Id(UUID userId);
    Optional<Cessation_Progress> findById(UUID id);
}

