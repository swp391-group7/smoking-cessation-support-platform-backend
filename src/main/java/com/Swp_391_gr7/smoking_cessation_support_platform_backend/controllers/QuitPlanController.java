// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/controllers/QuitPlanController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanCreateRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.QuitPlanService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/quit-plans")
@RequiredArgsConstructor
public class QuitPlanController {
    private final QuitPlanService quitPlanService;

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content)
    })
    @PostMapping("/create-plan")
    public ResponseEntity<QuitPlanDto> create(@RequestBody QuitPlanCreateRequest request) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        QuitPlanDto dto = quitPlanService.create(currentUserId, request);
        //return ResponseEntity.ok(quitPlanService.create(currentUserId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}/update-plan")
    public ResponseEntity<QuitPlanDto> update(@PathVariable UUID id, @RequestBody QuitPlanCreateRequest request) {
        return ResponseEntity.ok(quitPlanService.update(id, request));
    }

    @GetMapping("/{id}/display-plan")
    public ResponseEntity<QuitPlanDto> get(@PathVariable UUID id) {
        return ResponseEntity.ok(quitPlanService.get(id));
    }

    @DeleteMapping("/{id}/delete-plan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        quitPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}