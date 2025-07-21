package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT DISTINCT m.user.id
        FROM Membership_Package m
        WHERE m.isActive = true
          AND m.endDate > :now
    """)
    List<UUID> findDistinctUserIdsWithActiveMembership(
            @Param("now") LocalDateTime now
    );
    Optional<Membership_Package> findByUserIdAndIsActiveTrue(UUID userId);

    // … các method hiện tại …

    /**
     * 1. Lấy danh sách User DISTINCT đã có package được assign cho coach này
     */
    @Query("""
      SELECT DISTINCT m.user
      FROM Membership_Package m
      WHERE m.coach.userId = :coachId
    """)
    List<User> findDistinctUsersByCoachId(@Param("coachId") UUID coachId);

    /**
     * 2. Lấy tất cả Membership_Package của một user với coach cụ thể
     */
    List<Membership_Package> findAllByUserIdAndCoach_UserId(UUID userId, UUID coachId);
}
