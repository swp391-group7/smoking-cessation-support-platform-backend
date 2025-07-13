package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blog_posts")
public class Blog_Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_blogpost_user"))
    private User user;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "blog_type", length = 100)
    private String blogType;                     // Loại blog (ví dụ: experience, tips…)

    @Column(name = "img_url", length = 500)
    private String imgUrl;                       // URL ảnh đại diện (nếu có)

    @CreationTimestamp
    @Column (name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;


}