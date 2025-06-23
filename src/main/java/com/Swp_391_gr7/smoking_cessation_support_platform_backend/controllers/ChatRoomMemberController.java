// ChatRoomMemberController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoomMember;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroommember.ChatRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chatroom-members")
@RequiredArgsConstructor
public class ChatRoomMemberController {
    private final ChatRoomMemberService service;

    @PostMapping("/add")
    public ChatRoomMember add(@RequestParam UUID roomId, @RequestParam UUID userId) {
        return service.addMember(roomId, userId);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam UUID roomId, @RequestParam UUID userId) {
        service.removeMember(roomId, userId);
    }

    @GetMapping("/room/{roomId}")
    public List<ChatRoomMember> getMembers(@PathVariable UUID roomId) {
        return service.getMembers(roomId);
    }
}