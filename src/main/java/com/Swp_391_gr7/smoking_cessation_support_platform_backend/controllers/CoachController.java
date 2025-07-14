package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.CoachDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.UpdateCoachRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.coach.CoachService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coaches")
@RequiredArgsConstructor
public class CoachController {

    private final CoachService coachService;
    private final UserService userService;

    private ResponseEntity<Object> forbiddenResponse() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Bạn không có quyền thực hiện hành động này.");
    }

    private String getRole(Authentication auth) {
        UUID authId = UUID.fromString(auth.getName());
        return userService.getUserById(authId).getRoleName();
    }

    private UUID getAuthId(Authentication auth) {
        return UUID.fromString(auth.getName());
    }

    // --------------------------------------------------
    // CREATE
    // --------------------------------------------------
    @Operation(summary = "Tạo hồ sơ Coach")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coach được tạo thành công",
                    content = @Content(schema = @Schema(implementation = CoachDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền tạo"),
            @ApiResponse(responseCode = "409", description = "Coach đã tồn tại")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createCoach(
            @Valid @RequestBody CoachDto dto,
            Authentication authentication
    ) {
        String role = getRole(authentication);
        UUID authId = getAuthId(authentication);

        if ("coach".equalsIgnoreCase(role)) {
            // coach chỉ được tạo cho chính mình
            if (!authId.equals(dto.getUserId())) {
                return forbiddenResponse();
            }
        } else if (!"admin".equalsIgnoreCase(role)) {
            // chỉ admin và coach mới được tạo
            return forbiddenResponse();
        }

        CoachDto created = coachService.createCoach(dto.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // --------------------------------------------------
    // GET ONE
    // --------------------------------------------------
    @Operation(summary = "Lấy thông tin Coach theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thông tin Coach",
                    content = @Content(schema = @Schema(implementation = CoachDto.class))),
            @ApiResponse(responseCode = "404", description = "Coach không tồn tại")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getCoachById(@PathVariable UUID id) {
        CoachDto coach = coachService.getCoachById(id);
        return ResponseEntity.ok(coach);
    }

    // --------------------------------------------------
    // GET ALL
    // --------------------------------------------------
    @Operation(summary = "Lấy danh sách tất cả Coach")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Danh sách Coach",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CoachDto.class))))
    })
    @GetMapping("/all")
    public ResponseEntity<List<CoachDto>> getAllCoaches() {
        return ResponseEntity.ok(coachService.getAllCoaches());
    }

    // --------------------------------------------------
    // UPDATE
    // --------------------------------------------------
    @Operation(summary = "Cập nhật thông tin Coach")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coach đã được cập nhật",
                    content = @Content(schema = @Schema(implementation = CoachDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền cập nhật"),
            @ApiResponse(responseCode = "404", description = "Coach không tồn tại")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoach(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCoachRequest request,
            Authentication authentication
    ) {
        String role = getRole(authentication);
        UUID authId = getAuthId(authentication);

        if ("coach".equalsIgnoreCase(role) && !authId.equals(id)) {
            // coach chỉ được cập nhật mình
            return forbiddenResponse();
        } else if (!"admin".equalsIgnoreCase(role)) {
            // chỉ admin và coach mới được cập nhật
            return forbiddenResponse();
        }

        CoachDto updated = coachService.updateCoach(id, request);
        return ResponseEntity.ok(updated);
    }

    // --------------------------------------------------
    // DELETE
    // --------------------------------------------------
    @Operation(summary = "Xoá hồ sơ Coach")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xoá thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền xoá"),
            @ApiResponse(responseCode = "404", description = "Coach không tồn tại")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoach(
            @PathVariable UUID id,
            Authentication authentication
    ) {
        String role = getRole(authentication);
        UUID authId = getAuthId(authentication);

        if ("coach".equalsIgnoreCase(role) && !authId.equals(id)) {
            // coach chỉ được xoá mình
            return forbiddenResponse();
        } else if (!"admin".equalsIgnoreCase(role)) {
            // chỉ admin và coach mới được xoá
            return forbiddenResponse();
        }

        coachService.deleteCoach(id);
        return ResponseEntity.noContent().build();
    }
}
