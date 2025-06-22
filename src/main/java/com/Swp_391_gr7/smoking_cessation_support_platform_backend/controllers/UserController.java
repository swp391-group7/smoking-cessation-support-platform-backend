//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @Operation(summary = "Register a new user", description = "Tạo mới người dùng và gán vai trò tương ứng.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
//            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
//    })
//    @PostMapping("/register")
//    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody CreateUserRequest request) {
//        UserDto created = userService.createUser(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
//
//    @Operation(summary = "Get current user info", description = "Lấy thông tin người dùng hiện tại từ JWT.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(schema = @Schema(implementation = UserDto.class))),
//            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
//    })
//    @GetMapping("/get-current")
//    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
//        UUID userId = UUID.fromString(authentication.getName());
//        UserDto dto = userService.getUserById(userId);
//        return ResponseEntity.ok(dto);
//    }
//
//    @Operation(summary = "Update current user", description = "Cập nhật thông tin người dùng hiện tại.")
//    @PutMapping("/update")
//    public ResponseEntity<UserDto> updateCurrentUser(@Valid @RequestBody UpdateUserRequest request, Authentication authentication) {
//        UUID userId = UUID.fromString(authentication.getName());
//        UserDto updated = userService.updateUser(userId, request);
//        return ResponseEntity.ok(updated);
//    }
//
//    @Operation(summary = "Delete current user", description = "Xóa người dùng hiện tại.")
//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
//        UUID userId = UUID.fromString(authentication.getName());
//        userService.deleteUser(userId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @Operation(summary = "Get all users", description = "Lấy danh sách tất cả người dùng trong hệ thống.")
//    @GetMapping("/all")
//    public ResponseEntity<List<UserDto>> getAllUsers() {
//        List<UserDto> all = userService.getAllUsers();
//        return ResponseEntity.ok(all);
//    }
//
//    @Operation(summary = "Get users by role", description = "Lấy danh sách người dùng theo vai trò.")
//    @GetMapping("/by-role")
//    public ResponseEntity<List<UserDto>> getUsersByRole(@RequestParam String roleName) {
//        List<UserDto> users = userService.getUsersByRole(roleName);
//        return ResponseEntity.ok(users);
//    }
//
//    @Operation(summary = "Get user by ID", description = "Lấy thông tin người dùng theo ID.")
//    @GetMapping("/get/{userId}")
//    public ResponseEntity<UserDto> getUserById(@PathVariable UUID userId) {
//        UserDto dto = userService.getUserById(userId);
//        return ResponseEntity.ok(dto);
//    }
//}
