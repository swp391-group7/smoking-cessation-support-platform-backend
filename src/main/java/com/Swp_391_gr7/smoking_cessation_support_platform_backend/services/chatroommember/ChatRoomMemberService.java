// ChatRoomMemberService.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroommember;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoomMember;

import java.util.List;
import java.util.UUID;

public interface ChatRoomMemberService {
    ChatRoomMember addMember(UUID roomId, UUID userId);
    void removeMember(UUID roomId, UUID userId);
    List<ChatRoomMember> getMembers(UUID roomId);
}