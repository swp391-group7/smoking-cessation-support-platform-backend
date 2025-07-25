package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.CoachFeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackResponseDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.SystemFeedbackUpdateDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.feedBack.FeedBackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class FeedBackController {

    private final FeedBackService feedBackService;

    private UUID currentUserId() {
        // Giả sử principal lưu UUID userId
        return (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Operation(summary = "Create a new feedback",
            description = "Tạo mới feedback cho hệ thống hoặc coach.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<FeedbackResponseDTO> createFeedback(
            @Valid @RequestBody FeedbackRequestDTO req) {

        // Chỉ định user hiện tại làm userId
        req.setUserId(currentUserId());
        FeedbackResponseDTO dto = feedBackService.createFeedBack(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Get feedback by ID",
            description = "Lấy chi tiết feedback theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found feedback",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> getFeedbackById(
            @PathVariable UUID id) {

        FeedbackResponseDTO dto = feedBackService.getFeedBackById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all feedbacks",
            description = "Lấy danh sách tất cả feedback.")
    @ApiResponse(responseCode = "200", description = "List of feedbacks",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FeedbackResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<FeedbackResponseDTO>> getAllFeedback() {
        List<FeedbackResponseDTO> list = feedBackService.getAllFeedBack();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get current user's feedbacks",
            description = "Lấy tất cả feedback do user hiện tại gửi.")
    @ApiResponse(responseCode = "200", description = "User's feedbacks",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FeedbackResponseDTO.class)))
    @GetMapping("/me")
    public ResponseEntity<List<FeedbackResponseDTO>> getMyFeedback() {
        List<FeedbackResponseDTO> list =
                feedBackService.getFeedBackByUser(currentUserId());
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get feedbacks by target type",
            description = "Lấy feedback theo loại target (SYSTEM hoặc COACH).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid type", content = @Content)
    })
    @GetMapping("/target/{type}")
    public ResponseEntity<List<FeedbackResponseDTO>> getByTargetType(
            @PathVariable("type") String targetType) {

        List<FeedbackResponseDTO> list =
                feedBackService.getFeedBackByTargetType(targetType);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Get feedbacks for a coach",
            description = "Lấy tất cả feedback mà coach (theo coachId) nhận được.")
    @ApiResponse(responseCode = "200", description = "Coach's feedbacks",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FeedbackResponseDTO.class)))
    @GetMapping("/coach/{coachId}")
    public ResponseEntity<List<FeedbackResponseDTO>> getByCoach(
            @PathVariable UUID coachId) {

        List<FeedbackResponseDTO> list =
                feedBackService.getFeedBackByCoach(coachId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Update feedback",
            description = "Cập nhật nội dung hoặc rating của feedback.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateFeedback(
            @PathVariable UUID id,
            @Valid @RequestBody FeedbackRequestDTO req) {

        req.setUserId(currentUserId());
        FeedbackResponseDTO dto = feedBackService.updateFeedBack(id, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete feedback",
            description = "Xóa feedback theo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(
            @PathVariable UUID id) {

        feedBackService.deleteFeedBack(id);
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Feedback cho coach",
            description = "User feedback coach dựa trên gói membership đang active"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Feedback created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FeedbackResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input hoặc business rule",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Membership/Coach không tồn tại",
                    content = @Content)
    })
    @PostMapping("/coach")
    public ResponseEntity<FeedbackResponseDTO> feedbackCoach(
            @Valid @RequestBody CoachFeedbackRequestDTO dto) {

        UUID userId = currentUserId();
        FeedbackResponseDTO resp = feedBackService.feedbackCoach(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @Operation(summary = "Create system feedback",
            description = "User tạo feedback cho hệ thống, trả về avg system rating")
    @PostMapping("/system")
    public ResponseEntity<BigDecimal> createSystemFeedback(
            @Valid @RequestBody FeedbackRequestDTO req) {

        BigDecimal avg = feedBackService
                .createSystemFeedback(currentUserId(), req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(avg);
    }

    @PutMapping("/system")
    public ResponseEntity<FeedbackResponseDTO> updateSystemFeedback(
            @Valid @RequestBody FeedbackRequestDTO req) {

        FeedbackResponseDTO updated = feedBackService
                .updateSystemFeedback(currentUserId(), req);
        return ResponseEntity.ok(updated);
    }


    @GetMapping("/system/avg-rating")
    public ResponseEntity<BigDecimal> getSystemAvgRating() {
        return ResponseEntity.ok(feedBackService.computeSystemAvgRating());
    }
    @Operation(summary = "Update feedback cho coach",
            description = "Cập nhật feedback của coach từ gói membership active")
    @PutMapping("/coach")
    public ResponseEntity<FeedbackResponseDTO> updateCoachFeedback(
            @Valid @RequestBody FeedbackRequestDTO dto) {

        FeedbackResponseDTO updated = feedBackService
                .updateCoachFeedback(currentUserId(), dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/system/{userId}")
    public ResponseEntity<FeedbackResponseDTO> getSystemFeedback(@PathVariable UUID userId) {
        FeedbackResponseDTO dto = feedBackService.getSystemFeedbackByUser(userId);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/system/{id}")
    public ResponseEntity<FeedbackResponseDTO> updateSystemFeedback(
            @PathVariable("id") UUID id,
            @RequestBody SystemFeedbackUpdateDTO dto
    ) {
        FeedbackResponseDTO result = feedBackService.updateSystemFeedbackById(id, dto);
        return ResponseEntity.ok(result);
    }
}
