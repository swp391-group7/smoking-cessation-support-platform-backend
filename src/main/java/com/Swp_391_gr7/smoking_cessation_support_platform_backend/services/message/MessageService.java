// MessageService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.message;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatMessageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.SendMessageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message save(Message message);
    List<Message> getMessagesByRoom(UUID chatRoomId);
    ChatMessageDto createSendMessageRequest(UUID userId, UUID roomId, SendMessageRequest sendMessageRequest);
}