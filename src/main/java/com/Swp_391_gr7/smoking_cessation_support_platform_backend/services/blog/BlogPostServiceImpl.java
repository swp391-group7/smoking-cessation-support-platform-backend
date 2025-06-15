package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.blog;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.BlogPostDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.CreateBlogPostRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Blog_Post;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.BlogRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BlogPostServiceImpl implements BlogPostService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    public BlogPostDto create(UUID userId, CreateBlogPostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));
        Blog_Post entity = Blog_Post.builder()
                .user(user)
                .blogType(request.getBlogType())
                .title(request.getTitle())
                .content(request.getContent())
                .imgUrl(request.getImages())
                .build();
        Blog_Post saved = blogRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public BlogPostDto update(UUID id, CreateBlogPostRequest request, UUID userId) {
        Blog_Post existing = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found"));
        if (!existing.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to edit this blog post");
        }
        existing.setBlogType(request.getBlogType());
        existing.setTitle(request.getTitle());
        existing.setContent(request.getContent());
        existing.setImgUrl(request.getImages());
        Blog_Post updated = blogRepository.save(existing);
        return mapToDto(updated);
    }

    @Override
    public Optional<BlogPostDto> getById(UUID id) {
        return blogRepository.findById(id).map(this::mapToDto);
    }

    @Override
    public void delete(UUID id, UUID userId) {
        Blog_Post existing = blogRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blog post not found"));
        if (!existing.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed to delete this blog post");
        }
        blogRepository.delete(existing);
    }

    @Override
    public List<BlogPostDto> getAll() {
        return blogRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<BlogPostDto> searchByContentTitleOrUsername(String content, String username, String title) {
        List<Blog_Post> posts;
        if (content != null && !content.isEmpty()) {
            posts = blogRepository.findByContentContainingIgnoreCase(content);
        }else if (title != null && !title.isEmpty()) {
            posts = blogRepository.findByTitleContainingIgnoreCase(title);}
        else if (username != null && !username.isEmpty()) {
            posts = blogRepository.findByUser_UsernameContainingIgnoreCase(username);
        } else {
            posts = List.of();
        }
        return posts.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private BlogPostDto mapToDto(Blog_Post entity) {
        return BlogPostDto.builder()
                .id(entity.getId() != null ? entity.getId().toString() : null)
                .UserId(entity.getUser() != null ? entity.getUser().getId().toString() : null)
                .blog_type(entity.getBlogType())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImgUrl())
                .createdAt(entity.getCreateAt() != null ? entity.getCreateAt().toString() : null)
                .build();
    }
}