// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/controllers/QuitPlanStepController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.CreatePlanStepRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.QuitPlanStepDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlanStep.QuitPlanStepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/quit-plan_step")
@RequiredArgsConstructor
public class QuitPlanStepController {

    private final QuitPlanStepService stepService;

    @Operation(summary = "Lấy tất cả các bước trong một kế hoạch",
            description = "Trả về danh sách các bước theo thứ tự stepNumber thuộc về một kế hoạch cụ thể")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy kế hoạch", content = @Content)
    })
    @GetMapping("/{planId}/all")
    public ResponseEntity<List<QuitPlanStepDto>> getAllSteps(@PathVariable UUID planId) {
        List<QuitPlanStepDto> list = stepService.getStepsByPlanOrderByNumber(planId)
                .stream().map(step -> QuitPlanStepDto.builder()
                        .id(step.getId())
                        .quitPlanId(step.getPlan().getId())
                        .stepNumber(step.getStepNumber())
                        .stepStartDate(step.getStepStartDate())
                        .stepEndDate(step.getStepEndDate())
                        .targetCigarettesPerDay(step.getTargetCigarettesPerDay())
                        .stepDescription(step.getStepDescription())
                        .status(step.getStepStatus())
                        .createAt(step.getCreateAt())
                        .build()).toList();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Tạo bước mới trong kế hoạch",
            description = "Tạo một bước mới thuộc kế hoạch đã có, tự động đánh số thứ tự và kiểm tra ngày hợp lệ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo bước thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc ngày không đúng phạm vi", content = @Content)
    })
    @PostMapping("/{planId}/create")
    public ResponseEntity<QuitPlanStepDto> createStep(
            @PathVariable UUID planId,
            @Valid @RequestBody CreatePlanStepRequest request) {
        Quit_Plan_Step entity = Quit_Plan_Step.builder()
                .stepStartDate(request.getStepStartDate())
                .stepEndDate(request.getStepEndDate())
                .targetCigarettesPerDay(request.getTargetCigarettesPerDay())
                .stepDescription(request.getStepDescription())
                .stepStatus(request.getStatus())
                .build();

        Quit_Plan_Step saved = stepService.createStep(planId, entity);

        QuitPlanStepDto dto = QuitPlanStepDto.builder()
                .id(saved.getId())
                .quitPlanId(saved.getPlan().getId())
                .stepNumber(saved.getStepNumber())
                .stepStartDate(saved.getStepStartDate())
                .stepEndDate(saved.getStepEndDate())
                .targetCigarettesPerDay(saved.getTargetCigarettesPerDay())
                .stepDescription(saved.getStepDescription())
                .status(saved.getStepStatus())
                .createAt(saved.getCreateAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Cập nhật bước cụ thể",
            description = "Cập nhật thông tin chi tiết của một bước đã có thuộc kế hoạch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bước", content = @Content)
    })
    @PutMapping("/{planId}/update/{stepId}")
    public ResponseEntity<QuitPlanStepDto> updateStep(
            @PathVariable UUID planId,
            @PathVariable UUID stepId,
            @Valid @RequestBody CreatePlanStepRequest request) {
        Quit_Plan_Step entity = Quit_Plan_Step.builder()
                .stepStartDate(request.getStepStartDate())
                .stepEndDate(request.getStepEndDate())
                .stepNumber(request.getStepNumber())
                .targetCigarettesPerDay(request.getTargetCigarettesPerDay())
                .stepDescription(request.getStepDescription())
                .stepStatus(request.getStatus())
                .build();

        Quit_Plan_Step updated = stepService.updateStep(stepId, entity);

        QuitPlanStepDto dto = QuitPlanStepDto.builder()
                .id(updated.getId())
                .quitPlanId(updated.getPlan().getId())
                .stepNumber(updated.getStepNumber())
                .stepStartDate(updated.getStepStartDate())
                .stepEndDate(updated.getStepEndDate())
                .targetCigarettesPerDay(updated.getTargetCigarettesPerDay())
                .stepDescription(updated.getStepDescription())
                .status(updated.getStepStatus())
                .createAt(updated.getCreateAt())
                .build();

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Xoá bước",
            description = "Xoá một bước cụ thể trong kế hoạch")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xoá thành công", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bước", content = @Content)
    })
    @DeleteMapping("/{planId}/delete/{stepId}")
    public ResponseEntity<Void> deleteStep(
            @PathVariable UUID planId,
            @PathVariable UUID stepId) {
        stepService.deleteStep(stepId);
        return ResponseEntity.noContent().build();
    }
}
