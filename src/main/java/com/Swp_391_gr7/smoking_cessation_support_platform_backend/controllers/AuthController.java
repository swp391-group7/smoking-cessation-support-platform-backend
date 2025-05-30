package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.LoginRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.SigninRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
     private static final String ERROR_MESSAGE ="Invalid Username or Password";
     private final UserRepository userRepository;
     private final JWTService jwtService;
     private final PasswordEncoder passwordEncoder;

     @Operation (
             summary = "User Signup",
             description = "Creates a new user and returns a JWT token upon successful registration."
     )
     @ApiResponses(value = {
             @ApiResponse(
                     responseCode = "200",
                     description = "Token generated successfully",
                     content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
             ),
             @ApiResponse(
                     responseCode = "400",
                     description = "Username already existed",
                     content = @Content(mediaType = "text/plain")
             )
     })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SigninRequest signinRequest) {
         User user = User
                 .builder()
                 .username(signinRequest.getUsername())
                 .password(signinRequest.getPassword())
                 .fullName(signinRequest.getFullName())
                 .build();
         try {
             user = userRepository.save(user);
         }
         catch (Exception e){
                return ResponseEntity.badRequest().body("Username already existed");
         }
         final String token = jwtService.generateToken(user.getId());
         return ResponseEntity.ok(token);
     }

     @Operation(
             summary = "User Login",
             description = "Authenticates a user and returns a JWT token if the credentials are valid."
     )
    @ApiResponses(value={
            @ApiResponse(
                    responseCode ="200",
                    description = "Login successful, returns JWT token",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode ="401",
                    description =" Invalid username or password",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginrequest){
       final Optional<User> userOptional = userRepository.findByUsernameContainsIgnoreCase(loginrequest.getUsername());
        if (!userOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ERROR_MESSAGE);
        }
        final User user = userOptional.get();
        if (!passwordEncoder.matches(loginrequest.getPassword(), user.getPassword())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ERROR_MESSAGE);
        }
        final String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(token);
    }
}
