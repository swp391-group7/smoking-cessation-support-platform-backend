//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//public interface DailyLogRepository extends JpaRepository<Cessation_Progress, UUID>  {
//    List<Cessation_Progress> findByUserId(UUID userId);
//    Optional<Cessation_Progress> findByUserIdAndLogDate(UUID userId, LocalDate logDate);
//
//}
