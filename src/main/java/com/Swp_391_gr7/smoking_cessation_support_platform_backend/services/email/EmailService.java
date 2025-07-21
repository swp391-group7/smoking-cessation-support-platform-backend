package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.email;

public interface EmailService {
    void sendEmail(String to, String subject, String htmlContent);
}
