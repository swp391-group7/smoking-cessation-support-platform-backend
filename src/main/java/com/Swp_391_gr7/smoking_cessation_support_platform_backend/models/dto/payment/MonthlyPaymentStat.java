package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.payment;

import java.math.BigDecimal;

public record MonthlyPaymentStat(
        Integer     month,      // 1 = January … 12 = December
        BigDecimal totalAmount,
        long        count
) {}
