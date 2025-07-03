package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipPackageRepository extends JpaRepository<Membership_Package, UUID> {

    // Lấy tất cả các gói của một user
    List<Membership_Package> findAllByUserId(UUID userId);

    // Lấy gói mới nhất của user (dựa vào createAt)
    Optional<Membership_Package> findFirstByUserIdOrderByCreateAtDesc(UUID userId);

    // Lấy gói active mới nhất của user
    Optional<Membership_Package> findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(UUID userId);

    // Lấy tất cả gói đang active (nếu bạn cần lọc hết, không chỉ 1 user)
    List<Membership_Package> findAllByIsActiveTrue();

    // Kiểm tra xem user có gói nào còn hiệu lực tại thời điểm hiện tại
    Optional<Membership_Package> findFirstByUserIdAndIsActiveTrueAndEndDateAfterOrderByEndDateDesc(UUID userId, LocalDateTime now);

    List<Membership_Package> findAllByIsActiveTrueAndEndDateBefore(LocalDateTime date);

}
