// ChatRoomMemberServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.chatroommember;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoom;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.ChatRoomMember;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomMemberRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.ChatRoomRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberServiceImpl implements ChatRoomMemberService {
    private final ChatRoomMemberRepository repo;
    private final ChatRoomRepository chatRoomRepo;
    private final UserRepository userRepo;

    @Override
    public ChatRoomMember addMember(UUID roomId, UUID userId) {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(room)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();
        return repo.save(member);
    }

    @Override
    public void removeMember(UUID roomId, UUID userId) {
        repo.deleteByChatRoomIdAndUserId(roomId, userId);
    }

    @Override
    public List<ChatRoomMember> getMembers(UUID roomId) {
        return repo.findByChatRoomId(roomId);
    }
}