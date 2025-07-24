package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.FeedBack;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.FeedbackTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedBack, UUID> {

    // Lấy tất cả feedback của một user
    List<FeedBack> findByUser_Id(UUID userId);

    // Lấy feedback theo loại target (system hoặc coach)
    List<FeedBack> findByTargetType(FeedbackTarget targetType);

    // Lấy feedback của coach thông qua membershipPkg
    List<FeedBack> findByTargetTypeAndMembershipPackage_Id(FeedbackTarget targetType, UUID membershipPkgId);

    // Xóa tất cả feedback của một membership package (nếu cần)
    void deleteByMembershipPackage_Id(UUID membershipPkgId);


    /**
     * Lấy tất cả feedback liên quan đến coach có userId = coachUserId
     * (sử dụng thuộc tính userId trên Coach)
     */
    List<FeedBack> findByMembershipPackage_Coach_UserId(UUID coachUserId);

    /**
     * Lấy tất cả feedback theo kiểu target + coachUserId
     */
    List<FeedBack> findByTargetTypeAndMembershipPackage_Coach_UserId(
            FeedbackTarget targetType, UUID coachUserId);

    boolean existsByUser_IdAndTargetType(UUID userId, FeedbackTarget targetType);

    boolean existsByUser_IdAndMembershipPackage_Id(UUID userId, UUID pkgId);

    Optional<FeedBack> findByUser_IdAndTargetType(UUID userId, FeedbackTarget targetType);

    Optional<FeedBack> findByUser_IdAndMembershipPackage_Id(UUID userId, UUID membershipPkgId);


}
