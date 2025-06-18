// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/user/UserServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateuserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User updateUser(UUID userId, UpdateuserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getDob() != null) user.setDob(request.getDob());
        if (request.getAvtarPath() != null) user.setAvtarPath(request.getAvtarPath());
        return userRepository.save(user);
    }
    @Override
    public UserDto getUserInfo(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDob())
                .avtarPath(user.getAvtarPath())
                .providerId(user.getProviderId())
                .preStatus(user.getPreStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}