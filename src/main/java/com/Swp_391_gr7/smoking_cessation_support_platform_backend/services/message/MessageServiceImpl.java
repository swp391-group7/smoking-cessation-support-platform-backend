package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.message;


import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository repo;

    @Override
    public Message save(Message message) {
        return repo.save(message);
    }

    @Override
    public List<Message> getMessagesByRoom(UUID chatRoomId) {
        return repo.findByChatRoomIdOrderByCreateAtAsc(chatRoomId);
    }
}
