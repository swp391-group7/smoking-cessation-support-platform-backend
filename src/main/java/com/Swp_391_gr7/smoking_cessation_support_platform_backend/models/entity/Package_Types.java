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

    @Column(name = "price", nullable = false)
    private BigDecimal price; // Dùng BigDecimal cho tiền tệ chính xác

    @Column(name = "duration", nullable = false)
    private Integer duration; // Số ngày (ví dụ: 30 ngày)

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
