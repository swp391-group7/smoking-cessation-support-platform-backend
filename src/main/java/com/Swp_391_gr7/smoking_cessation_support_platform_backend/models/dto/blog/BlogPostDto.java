package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPostDto {
    private String id;
    private String UserId;
    private String blog_type;
    private String title;
    private String content;
    private String imageUrl;
    private String createdAt;
}
