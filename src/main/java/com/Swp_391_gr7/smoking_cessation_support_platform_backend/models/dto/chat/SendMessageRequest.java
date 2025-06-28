package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SendMessageRequest {

    private String content;
}