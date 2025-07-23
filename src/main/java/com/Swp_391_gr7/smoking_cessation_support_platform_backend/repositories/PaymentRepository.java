package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByTxnId(String txnId);

    List<Payment> findAllByStatus(String status);

    List<Payment> findAllByUser_Id(UUID userId);


    List<Payment> findAllByUser_IdAndStatus(UUID userId, String status);

    @Query("SELECT COALESCE(SUM(p.amount),0), COUNT(p) FROM Payment p")
    List<Object[]> fetchTotalAmountAndCount();


    @Query("""
      SELECT MONTH(p.payAt), COALESCE(SUM(p.amount),0), COUNT(p)
      FROM Payment p
      WHERE YEAR(p.payAt) = :year
      GROUP BY MONTH(p.payAt)
      """)
    List<Object[]> fetchMonthlyTotalsForYear(@Param("year") int year);
}

