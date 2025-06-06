package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.survey.CreateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.survey.UpdateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.SmokeSurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
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
    @PostMapping
    public ResponseEntity<SmokeSurveyDto> createSurvey(
            @RequestParam UUID userId,
            @Valid @RequestBody CreateSmokeSurveyRequest req) {
        SmokeSurveyDto dto = surveyService.createSurvey(userId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get Smoke Survey by User",
            description = "Lấy thông tin bảng khảo sát hút thuốc của user theo userId."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SmokeSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<SmokeSurveyDto> getSurvey(
            @RequestParam UUID userId) {
        SmokeSurveyDto dto = surveyService.getSurvey(userId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update Smoke Survey",
            description = "Cập nhật thông tin bảng khảo sát hút thuốc cho user."
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
    @PutMapping
    public ResponseEntity<SmokeSurveyDto> updateSurvey(
            @RequestParam UUID userId,
            @Valid @RequestBody UpdateSmokeSurveyRequest req) {
        SmokeSurveyDto dto = surveyService.updateSurvey(userId, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Delete Smoke Survey",
            description = "Xóa bảng khảo sát hút thuốc của user nếu có."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Survey deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteSurvey(
            @RequestParam UUID userId) {
        surveyService.deleteSurvey(userId);
        return ResponseEntity.noContent().build();
    }
}
