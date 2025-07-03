package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private UUID id;
    private UUID userId;
    private UUID packageTypeId;
    private BigDecimal amount;
    private String txnId;
    private String status;
    private LocalDateTime payAt;
}
