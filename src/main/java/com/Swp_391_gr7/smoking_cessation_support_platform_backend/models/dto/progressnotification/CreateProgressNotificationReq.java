package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProgressNotificationReq {
    private UUID planId;
    private String title;
    private String message;
    private String channel;
    private String type;
}
