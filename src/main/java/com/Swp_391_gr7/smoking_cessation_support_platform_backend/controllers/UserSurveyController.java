package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.CreateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UserSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UpdateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey.UseSurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user-surveys")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class UserSurveyController {

    private final UseSurveyService surveyService;
    private final UserService userService;

    private boolean isAdmin(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());

        UserDto dto = userService.getUserById(userId);
        return "admin".equalsIgnoreCase(dto.getRoleName());
    }

    @Operation(
            summary = "Create a new Smoke Survey",
            description = "Tạo mới bảng khảo sát hút thuốc cho user. Trả về dữ liệu DTO của survey vừa tạo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Survey created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/create-survey")
    public ResponseEntity<UserSurveyDto> createSurvey(
            @Valid @RequestBody CreateUserSurveyRequest req) {

        // Lấy userId trực tiếp từ JWT (do JwtAuthenticationFilter gán vào SecurityContext)
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        UserSurveyDto dto = surveyService.createSurvey(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Get Smoke Survey by User",
            description = "Lấy thông tin bảng khảo sát hút thuốc của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @GetMapping("/get-survey")
    public ResponseEntity<UserSurveyDto> getSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        UserSurveyDto dto = surveyService.getSurvey(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Update Smoke Survey",
            description = "Cập nhật thông tin bảng khảo sát hút thuốc cho user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @PutMapping("/update-survey")
    public ResponseEntity<UserSurveyDto> updateSurvey(
            @Valid @RequestBody UpdateUserSurveyRequest req) {

        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        UserSurveyDto dto = surveyService.updateSurvey(currentUserId, req);
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

    @Operation(
            summary = "Get First Smoke Survey of Current User",
            description = "Lấy khảo sát hút thuốc đầu tiên của user hiện tại (theo thời gian tạo)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Survey retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey not found for given userId",
                    content = @Content)
    })
    @GetMapping("/get-first-survey")
    public ResponseEntity<UserSurveyDto> getFirstSurvey() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        UserSurveyDto dto = surveyService.getFirstSurveyOfUser(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get All Smoke Surveys of Current User",
            description = "Lấy toàn bộ các khảo sát hút thuốc của user hiện tại."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Surveys retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "No surveys found for given userId",
                    content = @Content)
    })
    @GetMapping("/get-all-surveys")
    public ResponseEntity<List<UserSurveyDto>> getAllSurveys() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<UserSurveyDto> dtoList = surveyService.getAllSurveyOfUser(currentUserId);
        return ResponseEntity.ok(dtoList);
    }

    @Operation(
    summary = "Get All Smoke Surveys of a User",
    description = "Lấy toàn bộ các khảo sát hút thuốc của user hiện tại."
            )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Surveys retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserSurveyDto.class))),
            @ApiResponse(responseCode = "404", description = "No surveys found for given userId",
                    content = @Content)
    })
    @GetMapping("/get-all-surveys-of-user/{userId}")
    public ResponseEntity<List<UserSurveyDto>> getAllSurveysOfanUser(@PathVariable UUID userId) {

        List<UserSurveyDto> dtoList = surveyService.getAllSurveyOfUser(userId);
        return ResponseEntity.ok(dtoList);
    }


}

