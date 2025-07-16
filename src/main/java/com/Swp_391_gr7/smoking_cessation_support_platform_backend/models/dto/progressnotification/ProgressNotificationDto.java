package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressNotificationDto {
    private UUID id;
    private UUID planId;
    private String title;
    private String message;
    private String channel;
    private String type;
    private LocalDateTime sentAt;
    private Boolean isRead = false;
}
