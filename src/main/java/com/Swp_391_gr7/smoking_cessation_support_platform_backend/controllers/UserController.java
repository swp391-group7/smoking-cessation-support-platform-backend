package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    private boolean isAdmin(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UserDto dto = userService.getUserById(userId);
        return "admin".equalsIgnoreCase(dto.getRoleName());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest request, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to register users");
        }
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/get-current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to access this resource");
        }
        UUID userId = UUID.fromString(authentication.getName());
        UserDto dto = userService.getUserById(userId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to update users");
        }
        UUID userId = UUID.fromString(authentication.getName());
        UserDto updated = userService.updateUser(userId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCurrentUser(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to delete users");
        }
        UUID userId = UUID.fromString(authentication.getName());
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to view all users");
        }
        List<UserDto> all = userService.getAllUsers();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/by-role")
    public ResponseEntity<?> getUsersByRole(@RequestParam String roleName, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You're not authorized to view users by role");
        }
        List<UserDto> users = userService.getUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId, Authentication authentication) {

        UserDto dto = userService.getUserById(userId);
        return ResponseEntity.ok(dto);
    }
}
