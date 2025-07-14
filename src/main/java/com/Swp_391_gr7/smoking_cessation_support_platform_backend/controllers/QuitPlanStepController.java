package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.CreatePlanStepRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.QuitPlanStepDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.quitPlanStep.UpdatePLanStepRequest;
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
            description = "Trả về danh sách các bước theo thứ tự stepNumber của plan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy plan", content = @Content)
    })
    @GetMapping("/{planId}/all")
    public ResponseEntity<List<QuitPlanStepDto>> getAllSteps(@PathVariable UUID planId) {
        List<QuitPlanStepDto> dtos = stepService.getStepsByPlanOrderByNumber(planId)
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
                        .build())
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Tạo một bước mặc định (draft) trong kế hoạch",
            description = "Chỉ cần planId, service sẽ sinh step mới status=draft, stepNumber tự tăng.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo default step thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "400", description = "Plan không tồn tại", content = @Content)
    })
    @PostMapping("/{planId}/create-default")
    public ResponseEntity<QuitPlanStepDto> createDefaultStep(@PathVariable UUID planId) {
        Quit_Plan_Step saved = stepService.createDefaultStep(planId);
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

    @Operation(summary = "Tạo bước mới với dữ liệu tùy chỉnh",
            description = "Tạo step với thông tin gửi lên, giữ nguyên logic validation và auto-increment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo bước thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ hoặc ngày nằm ngoài plan", content = @Content)
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

    @Operation(summary = "Cập nhật bước theo stepNumber",
            description = "Cập nhật dữ liệu cho stepNumber trong plan và set status thành active")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = QuitPlanStepDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy step hoặc plan", content = @Content)
    })
    @PutMapping("/{planId}/update-by-number/{stepNumber}")
    public ResponseEntity<QuitPlanStepDto> updateStepByNumber(
            @PathVariable UUID planId,
            @PathVariable Integer stepNumber,
            @Valid @RequestBody UpdatePLanStepRequest request) {
        Quit_Plan_Step updated = stepService.updateStepByNumber(planId, stepNumber, request);
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

    @Operation(summary = "Xóa bước theo stepNumber",
            description = "Xóa stepNumber trong plan, đồng thời cập nhật lại thứ tự còn lại")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy step hoặc plan", content = @Content)
    })
    @DeleteMapping("/{planId}/delete-by-number/{stepNumber}")
    public ResponseEntity<Void> deleteStepByNumber(
            @PathVariable UUID planId,
            @PathVariable Integer stepNumber) {
        stepService.deleteStepByNumber(planId, stepNumber);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Xóa tất cả các bước có status = draft",
            description = "Dọn sạch các step draft trong một plan")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa draft thành công", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy plan", content = @Content)
    })
    @DeleteMapping("/{planId}/delete-drafts")
    public ResponseEntity<Void> deleteAllDrafts(@PathVariable UUID planId) {
        stepService.deleteAllDraftSteps(planId);
        return ResponseEntity.noContent().build();
    }

}
