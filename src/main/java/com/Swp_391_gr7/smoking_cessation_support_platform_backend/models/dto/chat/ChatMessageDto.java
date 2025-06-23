package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import lombok.Data;
import java.util.UUID;

@Data
public class ChatMessageDto {
    private UUID chatRoomId;
    private UUID senderId;
    private String content;
}