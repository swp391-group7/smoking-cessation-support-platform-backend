package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameContainsIgnoreCase(String username);
}
