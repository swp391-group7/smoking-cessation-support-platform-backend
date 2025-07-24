package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.MonthlyPaymentStat;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.PaymentSummary;
import com.paypal.api.payments.Payment;  // PayPal SDK
import com.paypal.base.rest.PayPalRESTException;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.CreatePaymentRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment.PaymentDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.payment.PaymentService;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PaymentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository paymentRepo;

    public PaymentController(PaymentService paymentService,
                             PaymentRepository paymentRepo) {
        this.paymentService = paymentService;
        this.paymentRepo    = paymentRepo;
    }

    /**
     * Tạo PayPal payment, lưu entity, rồi trả về approvalUrl + PaymentDto
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequest req) {
        try {
            // 1. Tạo payment bên PayPal và save nháp entity trong service
            Payment paypalPayment = paymentService.createPayment(
                    req.getUserId(),
                    req.getPackageTypeId(),
                    req.getCancelUrl(),
                    req.getSuccessUrl()
            );

            // 2. Lấy approvalUrl từ links
            String approvalUrl = paypalPayment.getLinks().stream()
                    .filter(l -> "approval_url".equalsIgnoreCase(l.getRel()))
                    .map(l -> l.getHref())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy approval_url"));

            // 3. Lấy lại entity của bạn từ DB qua txnId
            var entity = paymentRepo.findByTxnId(paypalPayment.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy payment record"));

            // 4. Map entity → DTO
            PaymentDto dto = PaymentDto.builder()
                    .id(entity.getId())
                    .userId(entity.getUser().getId())
                    .packageTypeId(entity.getPaymentPackage().getId())
                    .amount(entity.getAmount())
                    .txnId(entity.getTxnId())
                    .status(entity.getStatus())
                    .payAt(entity.getPayAt())
                    .build();

            // 5. Trả về FE
            Map<String,Object> body = new HashMap<>();
            body.put("approvalUrl", approvalUrl);
            body.put("payment", dto);
            return ResponseEntity.ok(body);

        } catch (PayPalRESTException e) {
            return ResponseEntity
                    .status(502)
                    .body(Map.of("error", "Lỗi khi tạo PayPal payment: " + e.getMessage()));
        }
    }

    /**
     * FE redirect về với paymentId & PayerID, gọi execute → trả về PaymentDto
     */
    @GetMapping("/execute")
    public ResponseEntity<?> executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID")   String payerId
    ) {
        try {
            paymentService.executePayment(paymentId, payerId);

            // Sau execute, DB đã update entity. Lấy ra để trả DTO:
            var entity = paymentRepo.findByTxnId(paymentId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy payment record"));

            PaymentDto dto = PaymentDto.builder()
                    .id(entity.getId())
                    .userId(entity.getUser().getId())
                    .packageTypeId(entity.getPaymentPackage().getId())
                    .amount(entity.getAmount())
                    .txnId(entity.getTxnId())
                    .status(entity.getStatus())
                    .payAt(entity.getPayAt())
                    .build();

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(502)
                    .body(Map.of("error", "Lỗi khi execute PayPal payment: " + e.getMessage()));
        }
    }

    /**
     * Lấy danh sách PaymentDto của 1 user, có thể filter theo status
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentDto>> getPaymentsByUser(
            @PathVariable UUID userId,
            @RequestParam(required = false) String status
    ) {
        var entities = (status == null || status.isBlank())
                ? paymentService.getAllPaymentsByUser(userId)
                : paymentService.getAllPaymentsByUserAndStatus(userId, status);

        List<PaymentDto> dtos = entities.stream().map(e ->
                PaymentDto.builder()
                        .id(e.getId())
                        .userId(e.getUser().getId())
                        .packageTypeId(e.getPaymentPackage().getId())
                        .amount(e.getAmount())
                        .txnId(e.getTxnId())
                        .status(e.getStatus())
                        .payAt(e.getPayAt())
                        .build()
        ).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/summary")
    public ResponseEntity<PaymentSummary> getSummary() {
        PaymentSummary summary = paymentService.getOverallSummary();
        return ResponseEntity.ok(summary);
    }

    /**
     * 2. Thống kê theo tháng trong 1 năm
     *    GET /api/payments/stats?year=2024
     */
    @GetMapping("/stats")
    public ResponseEntity<List<MonthlyPaymentStat>> getMonthlyStats(
            @RequestParam("year") int year
    ) {
        List<MonthlyPaymentStat> stats = paymentService.getMonthlyStats(year);
        return ResponseEntity.ok(stats);
    }
}
