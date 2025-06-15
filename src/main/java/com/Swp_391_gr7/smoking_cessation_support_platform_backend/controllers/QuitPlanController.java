package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan.QuitPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quit-plans")
@RequiredArgsConstructor
public class QuitPlanController {
    private final QuitPlanService quitPlanService;

    @Operation(summary = "Create a new Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/create-plan")
    public ResponseEntity<QuitPlanDto> create(@Valid @RequestBody QuitPlanCreateRequest req) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuitPlanDto dto = quitPlanService.create(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update a Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content)
    })
    @PutMapping("/{id}/edit-plan")
    public ResponseEntity<QuitPlanDto> update(@PathVariable UUID id, @Valid @RequestBody QuitPlanCreateRequest req) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QuitPlanDto dto = quitPlanService.update(id, req, currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get Quit Plan by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content)
    })
    @GetMapping("/{id}/search-plan-by-id")
    public ResponseEntity<QuitPlanDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(quitPlanService.getById(id));
    }

    @Operation(summary = "Delete a Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plan deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content)
    })
    @DeleteMapping("/{id}/delete-plan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        quitPlanService.delete(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all Quit Plans")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plans retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class)))
    })
    @GetMapping("/display-all-plans")
    public ResponseEntity<List<QuitPlanDto>> getAll() {
        return ResponseEntity.ok(quitPlanService.getAll());
    }

    @Operation(summary = "Search Quit Plans by method or status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plans retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<List<QuitPlanDto>> searchPlans(
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String status) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<QuitPlanDto> results = quitPlanService.searchByMethodOrStatus(method, status, currentUserId);
        return ResponseEntity.ok(results);
    }
}