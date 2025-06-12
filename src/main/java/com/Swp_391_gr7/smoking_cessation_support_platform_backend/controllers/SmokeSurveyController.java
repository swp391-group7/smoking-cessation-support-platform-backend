package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.SmokeSurveyService;
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
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SmokeSurveyController {

    private final SmokeSurveyService surveyService;

    @Operation(
            summary = "Create a new Smoke Survey",
            description = "Tạo mới bảng khảo sát hút thuốc cho user. Trả về dữ liệu DTO của survey vừa tạo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SmokeSurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/create-survey")
    public ResponseEntity<SmokeSurveyDto> createSurvey(
            @Valid @RequestBody CreateSmokeSurveyRequest req) {

        // Lấy userId trực tiếp từ JWT (do JwtAuthenticationFilter gán vào SecurityContext)
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SmokeSurveyDto dto = surveyService.createSurvey(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get Smoke Survey by User",
            description = "Lấy thông tin bảng khảo sát hút thuốc của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SmokeSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @GetMapping("/get-survey")
    public ResponseEntity<SmokeSurveyDto> getSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SmokeSurveyDto dto = surveyService.getSurvey(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update Smoke Survey",
            description = "Cập nhật thông tin bảng khảo sát hút thuốc cho user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SmokeSurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @PutMapping("/update-survey")
    public ResponseEntity<SmokeSurveyDto> updateSurvey(
            @Valid @RequestBody UpdateSmokeSurveyRequest req) {

        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SmokeSurveyDto dto = surveyService.updateSurvey(currentUserId, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete Smoke Survey",
            description = "Xóa bảng khảo sát hút thuốc của user hiện tại nếu có."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Survey deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @DeleteMapping("/delete-survey")
    public ResponseEntity<Void> deleteSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        surveyService.deleteSurvey(currentUserId);
        return ResponseEntity.noContent().build();
    }
}

