// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/configs/ChatWebSocketHandler.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.configs;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // Map<roomId, Set<session>>
    private final Map<String, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Optionally handle on connect
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Expecting JSON: {"type":"join/leave/message", "roomId":"...", "userId":"...", "content":"..."}
        String payload = message.getPayload();
        Map<String, String> msg = parseJson(payload);

        String type = msg.get("type");
        String roomId = msg.get("roomId");
        String userId = msg.get("userId");

        switch (type) {
            case "join" -> {
                chatRooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
                // Optionally notify others
            }
            case "leave" -> {
                Set<WebSocketSession> sessions = chatRooms.get(roomId);
                if (sessions != null) {
                    sessions.remove(session);
                }
            }
            case "message" -> {
                String content = msg.get("content");
                broadcast(roomId, userId, content);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatRooms.values().forEach(sessions -> sessions.remove(session));
    }

    private void broadcast(String roomId, String userId, String content) throws Exception {
        Set<WebSocketSession> sessions = chatRooms.get(roomId);
        if (sessions != null) {
            String json = "{\"userId\":\"" + userId + "\",\"content\":\"" + content + "\"}";
            for (WebSocketSession s : sessions) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(json));
                }
            }
        }
    }

    // Simple JSON parser for demo (use a real JSON lib in production)
    private Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replaceAll("[{}\"]", "");
        for (String pair : json.split(",")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) map.put(kv[0].trim(), kv[1].trim());
        }
        return map;
    }
}