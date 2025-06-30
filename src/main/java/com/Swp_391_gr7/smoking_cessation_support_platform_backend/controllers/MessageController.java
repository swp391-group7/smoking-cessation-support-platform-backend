package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatMessageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.SendMessageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Controller
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private ChatRoomRepository roomRepository;

    //for sending and receiving messages
    @GetMapping("/room/{roomId}/getMessages")
    public ResponseEntity<List<ChatMessageDto>> getRoomMessages(@PathVariable UUID roomId) {
        return ResponseEntity.ok(messageService.getMessagesByRoom(roomId));
    }

    @GetMapping("/room/{roomId}/newest")
    public List<ChatMessageDto> getMessagesByRoomNewest(@PathVariable UUID roomId) {
        return messageService.getMessagesByRoomNewest(roomId);
    }

    @PostMapping("/sendMessage/{roomId}/send")
    public ResponseEntity<ChatMessageDto> sendMessage(@PathVariable UUID roomId, @RequestBody SendMessageRequest mess) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UUID currentUserId = UUID.fromString(principal);
        ChatMessageDto dto = messageService.createSendMessageRequest(currentUserId,roomId, mess);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}