package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomMemberDto {
    private UUID id;
    private UUID chatRoomId;
    private UUID userId;
    private LocalDateTime joinedAt;
}
