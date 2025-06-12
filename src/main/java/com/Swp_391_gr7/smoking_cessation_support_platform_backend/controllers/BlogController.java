package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.BlogPostDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.blog.CreateBlogPostRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.BlogPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {
    private final BlogPostService blogPostService;

    @Operation(summary = "Create a new Blog Post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Blog created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogPostDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping("/create-blog")
    public ResponseEntity<BlogPostDto> create(@Valid @RequestBody CreateBlogPostRequest req) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogPostDto dto = blogPostService.create(currentUserId, req);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Update a Blog Post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogPostDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Blog not found", content = @Content)
    })
    @PutMapping("/{id}/edit-blog")
    public ResponseEntity<BlogPostDto> update(@PathVariable UUID id, @Valid @RequestBody CreateBlogPostRequest req) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BlogPostDto dto = blogPostService.update(id, req, currentUserId);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get Blog Post by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blog retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogPostDto.class))),
            @ApiResponse(responseCode = "404", description = "Blog not found", content = @Content)
    })
    @GetMapping("/{id}/search-blog-by-id")
    public ResponseEntity<BlogPostDto> getById(@PathVariable UUID id) {
        return blogPostService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a Blog Post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Blog deleted successfully", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Blog not found", content = @Content)
    })
    @DeleteMapping("/{id}/delete-blog")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID currentUserId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        blogPostService.delete(id, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all Blog Posts")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Blogs retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BlogPostDto.class)))
    })
    @GetMapping("/display-all-blog")
    public ResponseEntity<List<BlogPostDto>> getAll() {
        return ResponseEntity.ok(blogPostService.getAll());
    }
}