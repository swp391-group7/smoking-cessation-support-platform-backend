package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userbadge.UserBadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-badges")
@RequiredArgsConstructor
public class UserBadgeController {
    private final UserBadgeService userBadgesService;

    @Operation(summary = "Award a badge to a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Badge created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserBadgeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/assign")
    public ResponseEntity<UserBadgeDto> assignBadge(@RequestParam UUID userId, @RequestParam UUID badgeId) {
        UserBadgeDto dto = userBadgesService.assignBadge(userId, badgeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get all badges of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Badge created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserBadgeDto>> getUserBadges(@PathVariable UUID userId) {
        return ResponseEntity.ok(userBadgesService.getUserBadges(userId));
    }

    @Operation(summary = "Get all badges of the current user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Badge created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @GetMapping("/user/current")
    public ResponseEntity<List<UserBadgeDto>> getCurrentUserBadges() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return ResponseEntity.ok(userBadgesService.getUserBadges(currentUserId));
    }
}