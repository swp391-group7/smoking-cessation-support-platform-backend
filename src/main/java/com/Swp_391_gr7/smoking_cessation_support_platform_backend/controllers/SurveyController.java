package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey.SurveyService;
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
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(
            summary = "Create a new Survey",
            description = "Tạo mới một Survey cho user hiện tại và trả về SurveyDto vừa tạo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/create-survey")
    public ResponseEntity<SurveyDto> createSurvey(
            @Valid @RequestBody CreateSurveyRequest req
    ) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SurveyDto dto = surveyService.createSurvey(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get Survey by User",
            description = "Lấy Survey của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @GetMapping("/get-survey")
    public ResponseEntity<SurveyDto> getSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SurveyDto dto = surveyService.getSurvey(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update Survey",
            description = "Cập nhật Survey của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @PutMapping("/update-survey")
    public ResponseEntity<SurveyDto> updateSurvey(
            @Valid @RequestBody UpdateSurveyRequest req
    ) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        SurveyDto dto = surveyService.updateSurvey(currentUserId, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete Survey",
            description = "Xóa Survey của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Survey deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @DeleteMapping("/delete-survey")
    public ResponseEntity<Void> deleteSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        surveyService.deleteSurvey(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get Survey by ID",
            description = "Lấy Survey theo surveyId (admin hoặc use-case khác)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found", content = @Content)
    })
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto> getSurveyById(
            @PathVariable UUID surveyId
    ) {
        SurveyDto dto = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok(dto);
    }
}
