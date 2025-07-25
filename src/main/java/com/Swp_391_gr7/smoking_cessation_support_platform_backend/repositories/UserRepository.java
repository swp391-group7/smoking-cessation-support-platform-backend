package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

    // Lấy số người dùng theo tháng trong năm
    @Query("SELECT MONTH(u.createdAt) as month, COUNT(u) as count " +
            "FROM User u " +
            "WHERE YEAR(u.createdAt) = :year " +
            "GROUP BY MONTH(u.createdAt) " +
            "ORDER BY MONTH(u.createdAt)")
    List<Object[]> countUsersByMonthInYear(@Param("year") int year);

    // Lấy số người dùng theo giới tính
    @Query("SELECT u.sex, COUNT(u) FROM User u GROUP BY u.sex")
    List<Object[]> countUsersByGender();

    // Lấy tất cả người dùng có ngày sinh không null
    @Query("SELECT u FROM User u WHERE u.dob IS NOT NULL")
    List<User> findAllUsersWithDob();
    @Query("SELECT MONTH(u.createdAt), COUNT(u) FROM User u WHERE YEAR(u.createdAt) = :year AND u.role.id = :roleId GROUP BY MONTH(u.createdAt)")
    List<Object[]> countUsersByMonthInYearAndRole(@Param("year") int year, @Param("roleId") UUID roleId);
    @Query("SELECT u FROM User u WHERE u.dob IS NOT NULL AND u.role.id = :roleId")
    List<User> findAllUsersWithDobAndRole(@Param("roleId") UUID roleId);
    @Query("SELECT u.sex, COUNT(u) FROM User u WHERE u.role.id = :roleId GROUP BY u.sex")
    List<Object[]> countUsersByGenderAndRole(@Param("roleId") UUID roleId);

}
