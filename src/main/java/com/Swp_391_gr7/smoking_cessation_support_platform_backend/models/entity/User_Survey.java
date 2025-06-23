package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_survey")
public class User_Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_smoke_survey_user"))
    private User user;

    @Column(name = "smoke_duration", nullable = false)
    private String smokeDuration;

    @Column(name = "cigarettes_per_day", nullable = false)
    private Integer cigarettesPerDay;

    @Column(name = "price_each", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceEach;

    @Column(name = "tried_to_quit", nullable = false)
    private Boolean triedToQuit;

    @Column(name = "health_status", length = 255)
    private String healthStatus;

    // WHO survey answers a1–a8
    @Column(name = "a1", columnDefinition = "TEXT")
    private String a1;

    @Column(name = "a2", columnDefinition = "TEXT")
    private String a2;

    @Column(name = "a3", columnDefinition = "TEXT")
    private String a3;

    @Column(name = "a4", columnDefinition = "TEXT")
    private String a4;

    @Column(name = "a5", columnDefinition = "TEXT")
    private String a5;

    @Column(name = "a6", columnDefinition = "TEXT")
    private String a6;

    @Column(name = "a7", columnDefinition = "TEXT")
    private String a7;

    @Column(name = "a8", columnDefinition = "TEXT")
    private String a8;

    @Column(name = "dependency_level", nullable = false)
    @Min(1)
    @Max(5)
    private Integer dependencyLevel;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
