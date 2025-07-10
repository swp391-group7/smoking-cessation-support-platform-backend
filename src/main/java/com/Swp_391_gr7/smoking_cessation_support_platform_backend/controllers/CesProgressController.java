package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CesProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.UpdateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress.CesProgressServiceImpl;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cessation-progress")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
@Tag(name = "Cessation Progress", description = "API quản lý tiến trình cai thuốc lá")
public class CesProgressController {

    private final CesProgressServiceImpl cesProgressService;
    private final UserService userService;

    private boolean isAdmin(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UserDto dto = userService.getUserById(userId);
        return "admin".equalsIgnoreCase(dto.getRoleName());
    }

    @Operation(
            summary = "Create a new cessation progress",
            description = "Tạo mới tiến trình cai thuốc cho user trong một bước kế hoạch. Trả về dữ liệu DTO của tiến trình vừa tạo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Progress created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CesProgressDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<CesProgressDto> createProgress(
            @Valid @RequestBody CreateCesProgressRequest request) {

        CesProgressDto dto = cesProgressService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(
            summary = "Update cessation progress",
            description = "Cập nhật tiến trình cai thuốc đã tồn tại. Chỉ cập nhật các trường không null."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progress updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CesProgressDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid update data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @PutMapping("/update")
    public ResponseEntity<CesProgressDto> updateProgress(
            @Valid @RequestBody UpdateCesProgressRequest request) {

        CesProgressDto dto = cesProgressService.update(request);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get cessation progress by ID",
            description = "Lấy thông tin tiến trình cai thuốc theo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progress retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CesProgressDto.class))),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CesProgressDto> getProgressById(
            @Parameter(description = "ID của tiến trình cai thuốc", required = true)
            @PathVariable UUID id) {

        CesProgressDto dto = cesProgressService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(
            summary = "Get progress by plan step number",
            description = "Lấy danh sách tiến trình cai thuốc theo số thứ tự bước kế hoạch."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Progress list retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CesProgressDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/by-step/{stepNumber}")
    public ResponseEntity<List<CesProgressDto>> getProgressByStepNumber(
            @Parameter(description = "Số thứ tự bước kế hoạch", required = true)
            @PathVariable Integer stepNumber) {

        List<CesProgressDto> dtoList = cesProgressService.getByPlanStepNumber(stepNumber);
        return ResponseEntity.ok(dtoList);
    }

    @Operation(
            summary = "Delete cessation progress",
            description = "Xóa tiến trình cai thuốc theo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Progress deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Progress not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(
            @Parameter(description = "ID của tiến trình cai thuốc cần xóa", required = true)
            @PathVariable UUID id) {

        cesProgressService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get total cigarettes smoked today",
            description = "Lấy tổng số thuốc đã hút trong ngày hôm nay."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total cigarettes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/statistics/today")
    public ResponseEntity<Integer> getTotalCigarettesToday() {
        Integer total = cesProgressService.getTotalCigarettesToday();
        return ResponseEntity.ok(total);
    }

    @Operation(
            summary = "Get total cigarettes smoked by date",
            description = "Lấy tổng số thuốc đã hút trong một ngày cụ thể."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total cigarettes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid date format",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/statistics/by-date")
    public ResponseEntity<Integer> getTotalCigarettesByDate(
            @Parameter(description = "Ngày cần tính tổng (format: yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Integer total = cesProgressService.getTotalCigarettesByDate(date);
        return ResponseEntity.ok(total);
    }

    @Operation(
            summary = "Get total cigarettes smoked this week",
            description = "Lấy tổng số thuốc đã hút trong tuần này (7 ngày gần nhất)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total cigarettes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/statistics/this-week")
    public ResponseEntity<Integer> getTotalCigarettesThisWeek() {
        Integer total = cesProgressService.getTotalCigarettesThisWeek();
        return ResponseEntity.ok(total);
    }

    @Operation(
            summary = "Get cigarettes statistics by date range",
            description = "Lấy thống kê số thuốc đã hút theo từng ngày trong khoảng thời gian."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid date format or range",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/statistics/date-range")
    public ResponseEntity<Map<LocalDate, Integer>> getCigarettesByDateRange(
            @Parameter(description = "Ngày bắt đầu (format: yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Ngày kết thúc (format: yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Map<LocalDate, Integer> statistics = cesProgressService.getCigarettesByDateRange(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    @Operation(
            summary = "Get cigarettes statistics for last 30 days",
            description = "Lấy thống kê số thuốc đã hút trong 30 ngày gần nhất."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content)
    })
    @GetMapping("/statistics/last-30-days")
    public ResponseEntity<Map<LocalDate, Integer>> getCigarettesLast30Days() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29); // 30 ngày bao gồm hôm nay

        Map<LocalDate, Integer> statistics = cesProgressService.getCigarettesByDateRange(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
}