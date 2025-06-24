package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipPackageRepository extends JpaRepository<Membership_Package, UUID> {
    // Lấy gói thành viên active của 1 user
    Optional<Membership_Package> findByUserIdAndActive(UUID userId, boolean active);

    // Lấy tất cả các gói của user
    List<Membership_Package> findAllByUserId(UUID userId);

    // Lấy gói mới nhất của user (dựa vào thời gian tạo)
    Optional<Membership_Package> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);

    // Lấy gói đang active mới nhất
    Optional<Membership_Package> findFirstByUserIdAndActiveOrderByCreatedAtDesc(UUID userId, boolean active);


}
