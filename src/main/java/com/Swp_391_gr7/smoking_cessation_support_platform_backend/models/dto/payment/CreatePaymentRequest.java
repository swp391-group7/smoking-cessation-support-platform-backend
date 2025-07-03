package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatePaymentRequest {
    private UUID userId;
    private UUID packageTypeId;
    private String cancelUrl;   // URL FE redirect khi user bấm “hủy”
    private String successUrl;  // URL FE redirect khi thanh toán thành công
}
