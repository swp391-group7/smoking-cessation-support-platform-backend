package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.packagetype.PackageTypeService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/package-types")
@RequiredArgsConstructor
public class PackageTypeController {

    private final PackageTypeService packageTypeService;
    private final UserService userService;

    private ResponseEntity<Object> forbiddenResponse() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải là admin");
    }

    private boolean isNotAdmin(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return !"admin".equalsIgnoreCase(userService.getUserById(userId).getRoleName());
    }

    @Operation(summary = "Tạo một loại gói mới")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công", content = @Content(schema = @Schema(implementation = PackageTypeDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện")
    })
    @PostMapping
    public ResponseEntity<?> createPackageType(

            @Valid @RequestBody CreatePackageTypeRequest request,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        PackageTypeDto dto = packageTypeService.createPackageType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Cập nhật thông tin loại gói")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy loại gói")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackageType(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePackageTypeRequest request,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        PackageTypeDto dto = packageTypeService.updatePackageType(id, request);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Xóa một loại gói")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackageType(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        packageTypeService.deletePackageType(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lấy danh sách tất cả loại gói")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    })
    @GetMapping
    public ResponseEntity<?> getAllPackageTypes(Authentication authentication) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        List<PackageTypeDto> dtos = packageTypeService.getAllPackageTypes();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Lấy chi tiết một loại gói theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công", content = @Content(schema = @Schema(implementation = PackageTypeDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy loại gói")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageTypeById(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        PackageTypeDto dto = packageTypeService.getPackageTypeById(id);
        return ResponseEntity.ok(dto);
    }
}
