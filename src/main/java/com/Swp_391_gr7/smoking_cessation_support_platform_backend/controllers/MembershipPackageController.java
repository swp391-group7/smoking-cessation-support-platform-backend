package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.CreateMembershipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.MembershipPackageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.UpdateMemberShipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage.MembershipPackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/membership-packages")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = "*")
public class MembershipPackageController {

    private final MembershipPackageService membershipPackageService;

    @Operation(summary = "Tạo gói membership mới cho user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<MembershipPackageDto> createPackage(
            @Valid @RequestBody CreateMembershipPackageRequest request
    ) {
        UUID currentUserId = (UUID) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        // Lấy luôn packageTypeId từ request
        MembershipPackageDto dto = membershipPackageService.create(
                currentUserId,
                request.getPackageTypeId(),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @Operation(summary = "Kiểm tra user có gói active hay không")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trả về true nếu có, false nếu không"),
            @ApiResponse(responseCode = "404", description = "User không tồn tại hoặc lỗi tìm kiếm", content = @Content)
    })
    @GetMapping("/user/{userId}/has-active")
    public ResponseEntity<Boolean> hasActivePackage(
            @PathVariable UUID userId
    ) {
        boolean hasActive = membershipPackageService.hasActivePackageByUser(userId);
        return ResponseEntity.ok(hasActive);
    }
    @Operation(summary = "Lấy gói đang active của một user bất kỳ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói active", content = @Content)
    })
    @GetMapping("/user/{userId}/active-package")
    public ResponseEntity<MembershipPackageDto> getActivePackageByUserId(
            @PathVariable UUID userId
    ) {
        MembershipPackageDto dto = membershipPackageService.getActivePackageByUser(userId);
        return ResponseEntity.ok(dto);
    }

    // 2. Lấy danh sách tất cả userId có gói active
    @Operation(summary = "Lấy toàn bộ userId đang có gói active")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Danh sách userId"),
    })
    @GetMapping("/active/users")
    public ResponseEntity<List<UUID>> getAllUsersWithActivePackage() {
        List<UUID> activeUserIds = membershipPackageService.getAllActiveUserIds();
        return ResponseEntity.ok(activeUserIds);
    }

    @Operation(summary = "Cập nhật gói membership của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói membership", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MembershipPackageDto> updatePackage(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMemberShipPackageRequest request) {
        MembershipPackageDto dto = membershipPackageService.update(id, request);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Xóa gói membership của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công", content = @Content),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói membership", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePackage(@PathVariable UUID id) {
        membershipPackageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lấy thông tin một gói membership theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói membership", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MembershipPackageDto> getPackageById(@PathVariable UUID id) {
        MembershipPackageDto dto = membershipPackageService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lấy danh sách gói membership của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class)))
    })
    @GetMapping("/user")
    public ResponseEntity<List<MembershipPackageDto>> getAllForCurrentUser() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        List<MembershipPackageDto> list = membershipPackageService.getAllByUser(currentUserId);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "Lấy gói mới nhất của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói membership", content = @Content)
    })
    @GetMapping("/user/latest")
    public ResponseEntity<MembershipPackageDto> getLatestForCurrentUser() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        MembershipPackageDto dto = membershipPackageService.getLatestByUser(currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lấy gói đang active của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói active", content = @Content)
    })
    @GetMapping("/user/active")
    public ResponseEntity<MembershipPackageDto> getActiveForCurrentUser() {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        MembershipPackageDto dto = membershipPackageService.getActivePackageByUser(currentUserId);
        return ResponseEntity.ok(dto);
    }
    @Operation(summary = "Gán coach cho gói membership đang active của user hiện tại")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Gán coach thành công",
                    content = @Content(schema = @Schema(implementation = MembershipPackageDto.class))),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy gói hoặc coach", content = @Content)
    })
    @PutMapping("/active/assign-coach")
    public ResponseEntity<MembershipPackageDto> assignCoachToCurrentUser(
            @Valid @RequestBody UpdateMemberShipPackageRequest request
    ) {
        // Lấy userId từ token
        UUID currentUserId = (UUID) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        // Gọi service gán coach
        MembershipPackageDto updated = membershipPackageService
                .assignCoach(currentUserId, request.getCoachId());

        return ResponseEntity.ok(updated);
    }
}
