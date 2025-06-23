// ChatRoomService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroom;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.CreateChatRoomRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface ChatRoomService {
    ChatRoom createRoom(CreateChatRoomRequest room);
    List<ChatRoom> getAllRooms();
    ChatRoom getRoom(UUID id);
}