package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDetailDto; // ✥ import thêm
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey.SurveyService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;
    private final UserService userService;

    private ResponseEntity<Object> forbiddenResponse() {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Bạn không phải là admin");
    }

    private boolean isNotAdmin(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return !"admin".equalsIgnoreCase(userService.getUserById(userId).getRoleName());
    }

    @PostMapping("/create-survey")
    public ResponseEntity<?> createSurvey(
            @Valid @RequestBody CreateSurveyRequest req,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();

        UUID currentUserId = UUID.fromString(authentication.getName());
        SurveyDto dto = surveyService.createSurvey(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/get-survey")
    public ResponseEntity<?> getSurvey(Authentication authentication) {
        if (isNotAdmin(authentication)) return forbiddenResponse();

        UUID currentUserId = UUID.fromString(authentication.getName());
        SurveyDto dto = surveyService.getSurvey(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update-survey")
    public ResponseEntity<?> updateSurvey(
            @Valid @RequestBody UpdateSurveyRequest req,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();

        UUID currentUserId = UUID.fromString(authentication.getName());
        SurveyDto dto = surveyService.updateSurvey(currentUserId, req);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/delete-survey")
    public ResponseEntity<?> deleteSurvey(Authentication authentication) {
        if (isNotAdmin(authentication)) return forbiddenResponse();

        UUID currentUserId = UUID.fromString(authentication.getName());
        surveyService.deleteSurvey(currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{surveyId}")
    public ResponseEntity<?> getSurveyById(
            @PathVariable UUID surveyId,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();

        SurveyDto dto = surveyService.getSurveyById(surveyId);
        return ResponseEntity.ok(dto);
    }

    // Endpoint chi tiết Survey (bỏ qua kiểm tra role admin)
    @Operation(summary = "Lấy chi tiết survey, bao gồm câu hỏi và đáp án (bỏ qua kiểm tra admin)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trả về chi tiết survey",
                    content = @Content(schema = @Schema(implementation = SurveyDetailDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey không tìm thấy")
    })
    @GetMapping("/{surveyId}/detail")
    public ResponseEntity<?> getSurveyDetail(@PathVariable UUID surveyId) {
        SurveyDetailDto detailDto = surveyService.getSurveyDetail(surveyId);
        return ResponseEntity.ok(detailDto);
    }
}
