package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.AnswerDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.CreateAnswerRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.UpdateAnswerRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.answer.AnswerService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final UserService userService;

    private ResponseEntity<Object> forbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không phải là admin");
    }

    private boolean isNotAdmin(Authentication auth) {
        UUID uid = UUID.fromString(auth.getName());
        return !"admin".equalsIgnoreCase(userService.getUserById(uid).getRoleName());
    }

    @Operation(summary = "Tạo answer mới cho question")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công",
                    content = @Content(schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền" )
    })
    @PostMapping("/{questionId}/create")
    public ResponseEntity<?> createAnswer(
            @Parameter(description = "ID question") @PathVariable UUID questionId,
            @Valid @RequestBody CreateAnswerRequest req,
            Authentication auth
    ) {
        if (isNotAdmin(auth)) return forbidden();
        AnswerDto dto = answerService.createAnswer(questionId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Cập nhật answer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công",
                    content = @Content(schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền" ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy")
    })
    @PutMapping("/{answerId}")
    public ResponseEntity<?> updateAnswer(
            @Parameter(description = "ID answer") @PathVariable UUID answerId,
            @Valid @RequestBody UpdateAnswerRequest req,
            Authentication auth
    ) {
        if (isNotAdmin(auth)) return forbidden();
        AnswerDto dto = answerService.updateAnswer(answerId, req);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Xóa answer theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền" )
    })
    @DeleteMapping("/{answerId}")
    public ResponseEntity<?> deleteAnswer(
            @Parameter(description = "ID answer") @PathVariable UUID answerId,
            Authentication auth
    ) {
        if (isNotAdmin(auth)) return forbidden();
        answerService.deleteAnswer(answerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lấy answer theo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = AnswerDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền" ),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy")
    })
    @GetMapping("/{answerId}")
    public ResponseEntity<?> getAnswerById(
            @Parameter(description = "ID answer") @PathVariable UUID answerId,
            Authentication auth
    ) {
        if (isNotAdmin(auth)) return forbidden();
        AnswerDto dto = answerService.getAnswerById(answerId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Danh sách answers của question")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thành công") ,
            @ApiResponse(responseCode = "403", description = "Không có quyền" )
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<?> getAnswersByQuestion(
            @Parameter(description = "ID question") @PathVariable UUID questionId,
            Authentication auth
    ) {
        if (isNotAdmin(auth)) return forbidden();
        List<AnswerDto> list = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(list);
    }
}
