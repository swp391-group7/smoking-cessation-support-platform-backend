package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoomMember;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatMessageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomMemberRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MessageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;

    // --- WebSocket: Send message to chat room ---
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDto chatMessageDto, Principal principal) {
        // Save message to DB
        User sender = userRepository.findById(chatMessageDto.getSenderId()).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageDto.getChatRoomId()).orElseThrow();
        Message message = new Message();
        message.setSender(sender);
        message.setChatRoom(chatRoom);
        message.setContent(chatMessageDto.getContent());
        message.setCreateAt(LocalDateTime.now());
        messageRepository.save(message);

        // Broadcast to subscribers
        messagingTemplate.convertAndSend("/topic/chatroom/" + chatRoom.getId(), chatMessageDto);
    }

    // --- REST: Get chat history for a room ---
    @GetMapping("/rooms/{roomId}/messages")
    public List<Message> getChatHistory(@PathVariable UUID roomId) {
        return messageRepository.findByChatRoomIdOrderByCreateAtAsc(roomId);
    }

    // --- REST: Create a chat room ---
    @PostMapping("/rooms")
    public ChatRoom createChatRoom(@RequestBody ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    // --- REST: Get all chat rooms ---
    @GetMapping("/rooms")
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    // --- REST: Add member to chat room ---
    @PostMapping("/rooms/{roomId}/members")
    public ChatRoomMember addMember(@PathVariable UUID roomId, @RequestParam UUID userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        ChatRoomMember member = new ChatRoomMember();
        member.setChatRoom(chatRoom);
        member.setUser(user);
        member.setJoinedAt(LocalDateTime.now());
        return chatRoomMemberRepository.save(member);
    }

    // --- REST: Get members of a chat room ---
    @GetMapping("/rooms/{roomId}/members")
    public List<ChatRoomMember> getMembers(@PathVariable UUID roomId) {
        return chatRoomMemberRepository.findByChatRoomId(roomId);
    }

    // --- REST: Remove member from chat room ---
    @DeleteMapping("/rooms/{roomId}/members/{userId}")
    public void removeMember(@PathVariable UUID roomId, @PathVariable UUID userId) {
        chatRoomMemberRepository.deleteByChatRoomIdAndUserId(roomId, userId);
    }
}