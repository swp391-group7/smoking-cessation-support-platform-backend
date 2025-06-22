package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.role.RoleDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Create a new Role", description = "Tạo một vai trò mới.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Role created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid role", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<RoleDto> createRole(@RequestParam String roleName) {
        Role role = roleService.createRole(roleName);
        RoleDto dto = new RoleDto(role.getId(), role.getRole());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get all roles", description = "Lấy danh sách tất cả role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Roles retrieved successfully",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<RoleDto>> getAllRoles() {
        List<RoleDto> roles = roleService.getAllRoles().stream()
                .map(role -> new RoleDto(role.getId(), role.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Get Role by ID", description = "Lấy role theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable UUID id) {
        Role role = roleService.getById(id);
        RoleDto dto = new RoleDto(role.getId(), role.getRole());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get Role by name", description = "Lấy role theo tên.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RoleDto.class))),
            @ApiResponse(responseCode = "404", description = "Role not found", content = @Content)
    })
    @GetMapping("/by-name")
    public ResponseEntity<RoleDto> getRoleByName(@RequestParam String roleName) {
        Role role = roleService.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        RoleDto dto = new RoleDto(role.getId(), role.getRole());
        return ResponseEntity.ok(dto);
    }
}
