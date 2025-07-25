package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "badges")
public class Badges {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "badge_name", nullable = false, length = 50)
    private String badgeName;

    @Column(name = "badge_description", nullable = false, length = 255)
    private String badgeDescription;

    @Column(name="badge_image_url", nullable = false, length = 1000000)
    private String badgeImageUrl;

    @Column(name = "condition", nullable = false)
    private Integer condition; // Điều kiện để đạt được huy hiệu

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
