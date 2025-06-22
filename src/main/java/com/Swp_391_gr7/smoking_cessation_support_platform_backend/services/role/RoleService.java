package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.role;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleService {
    Role createRole(String roleName);
    Optional<Role> findByRole(String roleName);
    Role getById(UUID id);
    List<Role> getAllRoles();
}
