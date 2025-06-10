package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBlogPostRequest {
    @Size(max = 200)
    private String title;

    private String content;

    private String imageUrl;
}
