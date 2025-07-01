package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.notification.NotificationDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.usernotification.NotificationService;
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
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Create a new Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created successfully",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class)))
    })
    @PostMapping("/{userId}/notify-user")
    public ResponseEntity<NotificationDto> create(@PathVariable UUID userId, @Valid @RequestBody NotificationDto req) {
        NotificationDto dto = notificationService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update a Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> update(@PathVariable UUID id, @Valid @RequestBody NotificationDto req) {
        NotificationDto dto = notificationService.update(id, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete a Notification")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted successfully", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get Notification by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getById(@PathVariable UUID id) {
        NotificationDto dto = notificationService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all Notifications by User ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found",
                    content = @Content(schema = @Schema(implementation = NotificationDto.class)))
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<NotificationDto>> getByUserId(@PathVariable UUID userId) {
        List<NotificationDto> list = notificationService.getByUserId(userId);
        return ResponseEntity.ok(list);
    }
}
