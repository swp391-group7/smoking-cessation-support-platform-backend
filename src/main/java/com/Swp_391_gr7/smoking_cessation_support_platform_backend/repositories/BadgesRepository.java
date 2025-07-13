package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Badges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BadgesRepository extends JpaRepository<Badges, UUID> {
    // select * from badges where condition = :streak
    List<Badges> findAllByCondition(int condition);
}