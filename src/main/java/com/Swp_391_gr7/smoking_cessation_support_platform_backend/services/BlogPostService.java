package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.BlogPostDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.CreateBlogPostRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BlogPostService {
    BlogPostDto create(UUID userId, CreateBlogPostRequest request);
    BlogPostDto update(UUID id, CreateBlogPostRequest request, UUID userId);
    Optional<BlogPostDto> getById(UUID id);
    void delete(UUID id, UUID userId);
    List<BlogPostDto> getAll();
    List<BlogPostDto> searchByContentTitleOrUsername(String content, String title, String username);
}