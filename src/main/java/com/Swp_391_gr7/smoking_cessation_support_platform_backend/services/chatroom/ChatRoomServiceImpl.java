// ChatRoomServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroom;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.CreateChatRoomRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {
    private final ChatRoomRepository repo;

    @Override
    public ChatRoom createRoom(CreateChatRoomRequest room) {
        ChatRoom entity = ChatRoom.builder()
                .name(room.getName())
                .type(room.getType())
                .build();
        return repo.save(entity);
    }

    @Override
    public List<ChatRoom> getAllRooms() {
        return repo.findAll();
    }

    @Override
    public ChatRoom getRoom(UUID id) {
        return repo.findById(id).orElseThrow();
    }
}