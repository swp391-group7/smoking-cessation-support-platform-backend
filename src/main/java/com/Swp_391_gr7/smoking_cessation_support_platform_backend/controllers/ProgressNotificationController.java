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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/progress-notifications")
@RequiredArgsConstructor
public class ProgressNotificationController {
    private final ProgressNotificationService svc;
    private UUID currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Giả sử trong JWT bạn đã set principal là chuỗi UUID
        return UUID.fromString(auth.getName());
    }

    @Operation(summary = "Coach gửi remind hoặc chat cho user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notification created",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    @PostMapping("/{planId}/coach-notify")
    public ResponseEntity<ProgressNotificationDto> coachNotify(
            @PathVariable UUID planId,
            @Valid @RequestBody CreateProgressNotificationReq req) {

        UUID coachId = currentUserId();   // <-- đây
        ProgressNotificationDto dto = svc.coachNotify(coachId, planId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/{planId}/user-chat")
    public ResponseEntity<ProgressNotificationDto> userChat(
            @PathVariable UUID planId,
            @Valid @RequestBody CreateProgressNotificationReq req) {

        UUID userId = currentUserId();    // <-- và đây
        ProgressNotificationDto dto = svc.userChat(userId, planId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @Operation(summary = "Cập nhật notification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProgressNotificationDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProgressNotificationRequest req) {

        ProgressNotificationDto dto = svc.update(id, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Đánh dấu notification đã đọc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Marked as read",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @PutMapping("/{id}/read")
    public ResponseEntity<ProgressNotificationDto> markRead(@PathVariable UUID id) {
        ProgressNotificationDto dto = svc.changeStatus(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lấy tất cả notifications theo plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved",
                    content = @Content(schema = @Schema(implementation = ProgressNotificationDto.class)))
    })
    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<ProgressNotificationDto>> getByPlan(
            @PathVariable UUID planId) {

        List<ProgressNotificationDto> list = svc.getByPlanId(planId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lấy notifications theo type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<ProgressNotificationDto>> getByType(
            @PathVariable String type) {

        List<ProgressNotificationDto> list = svc.getByType(type);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lấy notifications theo channel")
    @GetMapping("/channel/{channel}")
    public ResponseEntity<List<ProgressNotificationDto>> getByChannel(
            @PathVariable String channel) {

        List<ProgressNotificationDto> list = svc.getByChannel(channel);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lấy tất cả remind mà coach đã gửi")
    @GetMapping("/coach/{coachId}/reminds")
    public ResponseEntity<List<ProgressNotificationDto>> getRemindsByCoach(
            @PathVariable UUID coachId) {

        List<ProgressNotificationDto> list = svc.getRemindsByCoach(coachId);
        return ResponseEntity.ok(list);
    }
}
