// ChatRoomController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.ChatRoomDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.chat.CreateChatRoomRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService service;

    @PostMapping
    public ChatRoom create(@RequestBody CreateChatRoomRequest room) {
        return service.createRoom(room);
    }

    @GetMapping
    public List<ChatRoomDto> getAll() {
        return service.getAllRooms();
    }

    @GetMapping("/{id}")
    public ChatRoom get(@PathVariable UUID id) {
        return service.getRoom(id);
    }
}