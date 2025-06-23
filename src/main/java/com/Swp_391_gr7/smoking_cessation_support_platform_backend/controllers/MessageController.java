// MessageController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Message;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @GetMapping("/room/{roomId}")
    public List<Message> getByRoom(@PathVariable UUID roomId) {
        return service.getMessagesByRoom(roomId);
    }

    @PostMapping
    public Message send(@RequestBody Message message) {
        return service.save(message);
    }
}