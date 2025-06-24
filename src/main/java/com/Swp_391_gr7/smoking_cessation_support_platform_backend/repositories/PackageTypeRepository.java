package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Package_Types;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PackageTypeRepository extends JpaRepository<Package_Types, UUID> {
    Optional<Package_Types> findByName(String name);

}
