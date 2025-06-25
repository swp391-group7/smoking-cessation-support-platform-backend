package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Coach;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CoachRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.RoleRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.coach.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CoachRepository coachRepository;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Role role = roleRepository.findByRole(request.getRoleName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Role not found: " + request.getRoleName()
                ));

        User newUser = User.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .dob(request.getDob())
                .avtarPath(request.getAvtarPath())
                .role(role)
                .build();

        User savedUser = userRepository.save(newUser);
        // Nếu role là COACH, tự động tạo hồ sơ Coach
        // Nếu là coach, tự tạo hồ sơ coach mặc định
        if ("coach".equalsIgnoreCase(savedUser.getRole().getRole())) {
            Coach coach = Coach.builder()
                    .user(savedUser)
                    .bio("")
                    .qualification("")
                    .avgRating(BigDecimal.ZERO)
                    .build();
            coachRepository.save(coach);
        }
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
        return mapToDto(u);
    }

    @Override
    public UserDto updateUser(UUID userId, UpdateUserRequest req) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if (req.getEmail() != null) u.setEmail(req.getEmail());
        if (req.getFullName() != null) u.setFullName(req.getFullName());
        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber());
        if (req.getDob() != null) u.setDob(req.getDob());
        if (req.getAvtarPath() != null) u.setAvtarPath(req.getAvtarPath());

        if (req.getRoleName() != null) {
            Role role = roleRepository.findByRole(req.getRoleName())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Role not found: " + req.getRoleName()
                    ));
            u.setRole(role);
        }

        User updated = userRepository.save(u);
        return mapToDto(updated);
    }

    @Override
    public void deleteUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
        userRepository.delete(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(String roleName) {
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role not found: " + roleName
                ));
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
    }

    private UserDto mapToDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .password(u.getPassword())
                .email(u.getEmail())
                .providerId(u.getProviderId())
                .fullName(u.getFullName())
                .phoneNumber(u.getPhoneNumber())
                .dob(u.getDob())
                .avtarPath(u.getAvtarPath())
                .preStatus(u.getPreStatus())
                .createdAt(u.getCreatedAt())
                .roleName(u.getRole() != null ? u.getRole().getRole() : null)
                .build();
    }
}
