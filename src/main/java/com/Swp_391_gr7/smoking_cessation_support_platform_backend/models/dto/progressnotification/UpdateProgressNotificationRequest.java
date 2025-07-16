// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/models/dto/progressnotification/UpdateProgressNotificationRequest.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProgressNotificationRequest {
    @NotBlank
    private String message;
    @NotBlank
    private String channel;
    @NotBlank
    private String type;
}
