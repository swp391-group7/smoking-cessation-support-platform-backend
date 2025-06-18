// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/controllers/UserController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateuserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update-peronal-info")
    public ResponseEntity<User> updateUser(
            @RequestBody UpdateuserRequest request,
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        User updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/display-current-user")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UserDto userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/display-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}