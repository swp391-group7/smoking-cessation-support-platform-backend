package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private UUID id;
    private String name;
    private String type;
    private LocalDateTime createAt;
}