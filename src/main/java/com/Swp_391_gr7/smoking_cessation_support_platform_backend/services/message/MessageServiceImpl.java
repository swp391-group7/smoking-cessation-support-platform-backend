package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.message;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatMessageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.SendMessageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MessageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository repo;
    private final UserRepository userRepository;


    @Override
    public Message save(Message message) {
        return repo.save(message);
    }

    @Override
    public List<ChatMessageDto> getMessagesByRoom(UUID chatRoomId) {
        return repo.findByChatRoomId(chatRoomId).stream().map(this::mapToDto).toList();
    }

    @Override
    public List<ChatMessageDto> getMessagesByRoomNewest(UUID chatRoomId) {
        List<Message> messages = repo.findByChatRoomIdOrderByCreateAtDesc(chatRoomId);
        return messages.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    @Override
    public ChatMessageDto createSendMessageRequest(UUID userId, UUID roomId, SendMessageRequest req) {
        if (userId == null || roomId == null || req.getContent() == null || req.getContent().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid message parameters");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
        Message message = Message.builder()
                .chatRoomId(roomId)
                .senderId(userId)
                .content(req.getContent())
                .build();
        Message saved = repo.save(message);
        return mapToDto(saved);
    }

    private ChatMessageDto mapToDto(Message entity) {
        return ChatMessageDto.builder()
                .id(entity.getId())
                .chatRoomId(entity.getChatRoomId())
                .senderId(entity.getSenderId())
                .content(entity.getContent())
                .createdAt(entity.getCreateAt())
                .build();
    }
}
