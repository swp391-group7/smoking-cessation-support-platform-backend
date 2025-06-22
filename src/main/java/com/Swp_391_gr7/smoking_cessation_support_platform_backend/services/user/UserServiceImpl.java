//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.RoleRepository;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class UserServiceImpl implements UserService {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public UserDto createUser(CreateUserRequest request) {
//        String hashedPassword = passwordEncoder.encode(request.getPassword());
//
//        User newUser = User.builder()
//                .username(request.getUsername())
//                .password(hashedPassword)
//                .email(request.getEmail())
//                .fullName(request.getFullName())
//                .phoneNumber(request.getPhoneNumber())
//                .dob(request.getDob())
//                .avtarPath(request.getAvtarPath())
//                .build();
//        User savedUser = userRepository.save(newUser);
//
//        Role role = Role.builder()
//                .role(request.getRoleName())
//                .user(savedUser)
//                .build();
//        roleRepository.save(role);
//
//        return mapToDto(savedUser);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public UserDto getUserById(UUID userId) {
//        User u = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found: " + userId
//                ));
//        return mapToDto(u);
//    }
//
//    @Override
//    public UserDto updateUser(UUID userId, UpdateUserRequest req) {
//        User u = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found: " + userId
//                ));
//
//        if (req.getPassword() != null && !req.getPassword().isBlank()) {
//            u.setPassword(passwordEncoder.encode(req.getPassword()));
//        }
//        if (req.getEmail() != null) u.setEmail(req.getEmail());
//        if (req.getFullName() != null) u.setFullName(req.getFullName());
//        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber());
//        if (req.getDob() != null) u.setDob(req.getDob());
//        if (req.getAvtarPath() != null) u.setAvtarPath(req.getAvtarPath());
//
//        User updated = userRepository.save(u);
//        return mapToDto(updated);
//    }
//
//    @Override
//    public void deleteUser(UUID userId) {
//        User u = userRepository.findById(userId)
//                .orElseThrow(() -> new ResponseStatusException(
//                        HttpStatus.NOT_FOUND, "User not found: " + userId
//                ));
//        userRepository.delete(u);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<UserDto> getUsersByRole(String roleName) {
//        List<Role> roles = roleRepository.findByRole(roleName);
//        if (roles.isEmpty()) {
//            return Collections.emptyList();
//        }
//
//        // Lấy tất cả users từ các Role, ánh xạ sang UserDto
//        return roles.stream()
//                .map(Role::getUser)        // lấy user từ mỗi role
//                .map(this::mapToDto)       // ánh xạ sang DTO
//                .toList();                 // Java 16+, hoặc dùng .collect(Collectors.toList())
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<UserDto> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }
//
//    private UserDto mapToDto(User u) {
//        return UserDto.builder()
//                .id(u.getId())
//                .username(u.getUsername())
//                .password(u.getPassword())
//                .email(u.getEmail())
//                .providerId(u.getProviderId())
//                .fullName(u.getFullName())
//                .phoneNumber(u.getPhoneNumber())
//                .dob(u.getDob())
//                .avtarPath(u.getAvtarPath())
//                .preStatus(u.getPreStatus())
//                .createdAt(u.getCreatedAt())
//                .build();
//    }
//}