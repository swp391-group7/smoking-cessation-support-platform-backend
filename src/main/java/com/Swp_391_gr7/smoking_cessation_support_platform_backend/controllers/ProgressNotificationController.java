package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.CreateProgressNotificationReq;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.ProgressNotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification.UpdateProgressNotificationRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.progressnotification.ProgressNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/progress-notifications")
@RequiredArgsConstructor
public class ProgressNotificationController {
    private final ProgressNotificationService progressNotificationService;

    @Operation(summary = "Create a new Progress Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfully",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @PostMapping
    public ResponseEntity<ProgressNotificationDto> create(@Valid @RequestBody CreateProgressNotificationReq req) {
        ProgressNotificationDto dto = progressNotificationService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update a Progress Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProgressNotificationDto> update(@PathVariable UUID id, @Valid @RequestBody UpdateProgressNotificationRequest req) {
        ProgressNotificationDto dto = progressNotificationService.update(id, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete a Progress Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted successfully", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        progressNotificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change read status of a Progress Notification (mark as read)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status changed",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @PutMapping("/{id}/read")
    public ResponseEntity<ProgressNotificationDto> changeStatus(@PathVariable UUID id) {
        ProgressNotificationDto dto = progressNotificationService.changeStatus(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get Progress Notification by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProgressNotificationDto> getById(@PathVariable UUID id) {
        ProgressNotificationDto dto = progressNotificationService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all Progress Notifications by Plan ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @GetMapping("/by-plan/{planId}")
    public ResponseEntity<List<ProgressNotificationDto>> getByPlanId(@PathVariable UUID planId) {
        List<ProgressNotificationDto> list = progressNotificationService.getByPlanId(planId);
        return ResponseEntity.ok(list);
    }
}
