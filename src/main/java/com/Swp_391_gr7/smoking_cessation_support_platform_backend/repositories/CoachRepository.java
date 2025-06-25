package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CoachRepository extends JpaRepository<Coach, UUID> {

}
