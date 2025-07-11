package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Blog_Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlogRepository extends JpaRepository<Blog_Post, UUID> {
    Optional<Blog_Post> findByUserId(UUID userId);
    Optional<Blog_Post> findByContent(String blogType);
    List<Blog_Post> findByContentContainingIgnoreCase(String content);
    List<Blog_Post> findByUser_UsernameContainingIgnoreCase(String username);
    List<Blog_Post> findByTitleContainingIgnoreCase(String title);
}
