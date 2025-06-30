package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "package_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package_Types {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name; // VD: "Premium", "Free", "Pro"

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "des1", columnDefinition = "TEXT")
    private String des1; // Mô tả ngắn gọn về gói, có thể là các tính năng chính

    @Column(name = "des2", columnDefinition = "TEXT")
    private String des2; // Mô tả ngắn gọn về gói, có thể là các tính năng chính

    @Column(name = "des3", columnDefinition = "TEXT")
    private String des3; // Mô tả ngắn gọn về gói, có thể là các tính năng chính

    @Column(name = "des4", columnDefinition = "TEXT")
    private String des4; // Mô tả ngắn gọn về gói, có thể là các tính năng chính

    @Column(name = "des5", columnDefinition = "TEXT")
    private String des5; // Mô tả ngắn gọn về gói, có thể là các tính năng chính

    @Column(name = "price", nullable = false)
    private BigDecimal price; // Dùng BigDecimal cho tiền tệ chính xác

    @Column(name = "duration", nullable = false)
    private Integer duration; // Số ngày (ví dụ: 30 ngày)

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
