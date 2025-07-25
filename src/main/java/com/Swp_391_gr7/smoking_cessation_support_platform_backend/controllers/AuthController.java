package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.LoginRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.SigninRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.RoleRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.jwt.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String ERROR_MESSAGE = "Invalid Username or Password";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "User Signup",
            description = "Creates a new user and returns a JWT token upon successful registration."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token generated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Username already existed or role missing",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            value = "/signup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> signup(@RequestBody SigninRequest signinRequest) {
        if (userRepository.findByUsernameIgnoreCase(signinRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username already existed"));
        }

        // ✅ Tìm role 'user' từ DB (phải được seed trước)
        Role userRole = roleRepository.findByRole("user")
                .orElseThrow(() -> new RuntimeException("Default role 'user' not found"));

        // ✅ Tạo user
        String hashedPassword = passwordEncoder.encode(signinRequest.getPassword());
        User user = User.builder()
                .username(signinRequest.getUsername())
                .password(hashedPassword)
                .fullName(signinRequest.getFullName())
                .email(signinRequest.getEmail())
                .dob(signinRequest.getDob())
                .sex(signinRequest.getSex())
                .phoneNumber(signinRequest.getPhoneNumber())
                .role(userRole)
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId());
        Map<String, String> responseBody = Collections.singletonMap("token", token);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token if the credentials are valid."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns JWT token and user info",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository
                .findByUsernameIgnoreCase(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", ERROR_MESSAGE));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", ERROR_MESSAGE));
        }

        String token = jwtService.generateToken(user.getId());

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("id", user.getId());
        userPayload.put("full_name", user.getFullName());
        userPayload.put("avatar_path", user.getAvtarPath());
        userPayload.put("role", user.getRole() != null ? user.getRole().getRole() : null);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", userPayload);

        return ResponseEntity.ok(responseBody);
    }
}
