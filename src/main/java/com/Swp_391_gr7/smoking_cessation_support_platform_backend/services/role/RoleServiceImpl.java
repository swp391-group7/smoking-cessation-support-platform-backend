package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.role;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(String roleName) {
        if (roleRepository.existsByRole(roleName)) {
            throw new RuntimeException("Role already exists: " + roleName);
        }
        Role role = Role.builder()
                .role(roleName)
                .build();
        return roleRepository.save(role);
    }

    @Override
    public Optional<Role> findByRole(String roleName) {
        return roleRepository.findByRole(roleName);
    }

    @Override
    public Role getById(UUID id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
