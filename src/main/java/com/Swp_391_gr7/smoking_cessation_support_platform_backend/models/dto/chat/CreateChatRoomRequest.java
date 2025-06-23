package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateChatRoomRequest {
    private String name;
    private String type;
}
