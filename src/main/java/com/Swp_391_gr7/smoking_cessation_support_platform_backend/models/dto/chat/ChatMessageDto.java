package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ChatMessageDto {
    private UUID id;
    private UUID chatRoomId;
    private UUID senderId;
    private String content;
    private LocalDateTime createdAt;
}