// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/models/dto/progressnotification/ProgressNotificationDto.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressNotificationDto {
    private UUID id;
    private UUID planId;
    private String message;
    private String channel;   // "push" | "email"
    private String type;      // "remind" | "chat"
    private LocalDateTime sentAt;
    private UUID senderId;      // ← thêm
    private UUID recipientId;
    private Boolean isRead;
}
