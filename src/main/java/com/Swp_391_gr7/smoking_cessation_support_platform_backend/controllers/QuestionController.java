package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.CreateQuestionRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.QuestionDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.UpdateQuestionRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.question.QuestionService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
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

    @Operation(summary = "Tạo câu hỏi mới cho một khảo sát")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tạo câu hỏi thành công",
                    content = @Content(schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện")
    })
    @PostMapping("/{surveyId}/create")
    public ResponseEntity<?> createQuestion(
            @Parameter(description = "ID của khảo sát") @PathVariable UUID surveyId,
            @Valid @RequestBody CreateQuestionRequest request,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        QuestionDto dto = questionService.createQuestion(surveyId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Cập nhật nội dung câu hỏi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy câu hỏi")
    })
    @PutMapping("/{questionId}")
    public ResponseEntity<?> updateQuestion(
            @Parameter(description = "ID của câu hỏi cần cập nhật") @PathVariable UUID questionId,
            @Valid @RequestBody UpdateQuestionRequest request,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        QuestionDto dto = questionService.updateQuestion(questionId, request);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Xóa một câu hỏi theo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện")
    })
    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @Parameter(description = "ID của câu hỏi cần xóa") @PathVariable UUID questionId,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Lấy thông tin chi tiết của một câu hỏi")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thành công",
                    content = @Content(schema = @Schema(implementation = QuestionDto.class))),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy câu hỏi")
    })
    @GetMapping("/{questionId}")
    public ResponseEntity<?> getQuestionById(
            @Parameter(description = "ID của câu hỏi") @PathVariable UUID questionId,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        QuestionDto dto = questionService.getQuestionById(questionId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Lấy danh sách câu hỏi của một khảo sát")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công"),
            @ApiResponse(responseCode = "403", description = "Không có quyền thực hiện")
    })
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<?> getQuestionsBySurvey(
            @Parameter(description = "ID của khảo sát") @PathVariable UUID surveyId,
            Authentication authentication
    ) {
        if (isNotAdmin(authentication)) return forbiddenResponse();
        List<QuestionDto> dtos = questionService.getQuestionsBySurveyId(surveyId);
        return ResponseEntity.ok(dtos);
    }
}
