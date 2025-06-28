// ChatRoomServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroom;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatRoomDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.CreateChatRoomRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public List<ChatRoomDto> getAllRooms() {
        return repo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ChatRoom getRoom(UUID id) {
        return repo.findById(id).orElseThrow();
    }

    private ChatRoomDto mapToDto(ChatRoom entity) {
        return ChatRoomDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType())
                .createAt(entity.getCreateAt())
                .build();
    }

}