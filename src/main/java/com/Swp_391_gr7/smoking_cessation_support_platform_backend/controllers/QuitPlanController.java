package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.UpdateQuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan.QuitPlanService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;



    private UUID getCurrentUserId() {
        return (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Operation(summary = "Create a new Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/create-plan")
    public ResponseEntity<QuitPlanDto> create(@Valid @RequestBody QuitPlanCreateRequest req) {
        QuitPlanDto dto = quitPlanService.create(getCurrentUserId(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Generate a Quit Plan from Survey")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan generated from survey",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "404", description = "Survey or User not found", content = @Content)
    })
    @PostMapping("/generate-from-survey/{surveyId}")
    public ResponseEntity<QuitPlanDto> generateFromSurvey(@PathVariable UUID surveyId) {
        QuitPlanDto dto = quitPlanService.generatePlanFromSurvey(getCurrentUserId(), surveyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Create an immediate (active) Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Immediate plan created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class)))
    })
    @PostMapping("/create-immediate-plan")
    public ResponseEntity<QuitPlanDto> createImmediate() {
        QuitPlanDto dto = quitPlanService.createImmediatePlan(getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Create a draft Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Draft plan created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class)))
    })
    @PostMapping("/create-draft-plan")
    public ResponseEntity<QuitPlanDto> createDraft() {
        QuitPlanDto dto = quitPlanService.createDraftPlan(getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update the latest draft Quit Plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Draft updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "404", description = "No draft found", content = @Content)
    })
    @PutMapping("/update-latest-draft")
    public ResponseEntity<QuitPlanDto> updateLatestDraft(@Valid @RequestBody UpdateQuitPlanRequest req) {
        QuitPlanDto dto = quitPlanService.updateLatestDraft(getCurrentUserId(), req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Delete all draft Quit Plans of current user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Drafts deleted successfully", content = @Content)
    })
    @DeleteMapping("/delete-all-drafts")
    public ResponseEntity<Void> deleteAllDrafts() {
        quitPlanService.deleteAllDrafts(getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

//    @Operation(summary = "Update an existing Quit Plan")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Plan updated successfully",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = QuitPlanDto.class))),
//            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
//            @ApiResponse(responseCode = "404", description = "Plan not found", content = @Content)
//    })
//    @PutMapping("/{id}/edit-plan")
//    public ResponseEntity<QuitPlanDto> update(@PathVariable UUID id, @Valid @RequestBody QuitPlanCreateRequest req) {
//        QuitPlanDto dto = quitPlanService.update(id, req, getCurrentUserId());
//        return ResponseEntity.ok(dto);
//    }

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
        quitPlanService.delete(id, getCurrentUserId());
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
        List<QuitPlanDto> results = quitPlanService.searchByMethodOrStatus(method, status, getCurrentUserId());
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Get active Quit Plan of current user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active plan retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "404", description = "No active plan found", content = @Content)
    })
    @GetMapping("/active")
    public ResponseEntity<QuitPlanDto> getActivePlan() {
        QuitPlanDto dto = quitPlanService.getActivePlanByUserId(getCurrentUserId());
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get active Quit Plan of a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Active plan retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "404", description = "No active plan found", content = @Content)
    })
    @GetMapping("/active/{userId}")
    public ResponseEntity<QuitPlanDto> getActivePlan(@PathVariable UUID userId) {
        QuitPlanDto dto = quitPlanService.getActivePlanByUserId(userId);
        return ResponseEntity.ok(dto);
    }
}
