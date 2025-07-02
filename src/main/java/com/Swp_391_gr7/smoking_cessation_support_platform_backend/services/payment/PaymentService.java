package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.payment;

import com.paypal.api.payments.Payment;              // đây là PayPalPayment
import com.paypal.base.rest.PayPalRESTException;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    // Tạo payment trên PayPal
    Payment createPayment(UUID userId,
                          UUID packageTypeId,
                          String cancelUrl,
                          String successUrl) throws PayPalRESTException;

    // Thực thi payment
    Payment executePayment(String paymentId,
                           String payerId) throws PayPalRESTException;

    // Lấy tất cả payment (entity của bạn) của 1 user
    List<com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment>
    getAllPaymentsByUser(UUID userId);

    // Lấy tất cả payment của user theo status
    List<com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Payment>
    getAllPaymentsByUserAndStatus(UUID userId, String status);
}
