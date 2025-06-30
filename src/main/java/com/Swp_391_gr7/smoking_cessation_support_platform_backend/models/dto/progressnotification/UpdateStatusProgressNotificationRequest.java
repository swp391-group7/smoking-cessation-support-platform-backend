package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.progressnotification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusProgressNotificationRequest {
    private boolean isRead;
}
