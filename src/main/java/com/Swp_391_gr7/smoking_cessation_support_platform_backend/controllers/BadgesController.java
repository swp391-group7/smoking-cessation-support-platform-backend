package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeCreationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.BlogPostDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.badge.BadgesService;
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
@RequestMapping("/badges")
@RequiredArgsConstructor
public class BadgesController {
    private final BadgesService badgesService;

    @Operation(summary = "Create a new Badge")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Badge created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/create-badge")
    public ResponseEntity<BadgeDto> create(@Valid @RequestBody BadgeCreationRequest req) {
        BadgeDto dto = badgesService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get all Badges")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badges retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDto.class)))
    })
    @GetMapping("/display-all")
    public ResponseEntity<List<BadgeDto>> getAll() {
        return ResponseEntity.ok(badgesService.getAll());
    }

    @Operation(summary = "Get Badge by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Badge retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDto.class))),
            @ApiResponse(responseCode = "404", description = "Badge not found", content = @Content)
    })
    @GetMapping("/{id}/display-badge")
    public ResponseEntity<BadgeDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(badgesService.getById(id));
    }

    @Operation(summary = "Delete a Badge")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Badge deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Badge not found", content = @Content)
    })
    @DeleteMapping("/{id}/delete-badge")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        badgesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}