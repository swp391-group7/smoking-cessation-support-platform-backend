package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByRole(String role);

    boolean existsByRole(String role);
}
