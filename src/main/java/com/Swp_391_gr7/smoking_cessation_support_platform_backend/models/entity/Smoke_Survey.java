package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
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
@Table(name="smoke_survey")
public class Smoke_Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Version
    private Long version;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_smoke_survey_user"))
    private User user;

    @Column(name = "smoke_duration", nullable = false)
    private Integer smokeDuration;

    @Column(name = "cigarettes_per_day", nullable = false)
    private Integer cigarettesPerDay;

    @Column(name = "price_each", precision = 10, scale = 2, nullable = false)
    private BigDecimal priceEach;

    @Column(name = "tried_to_quit", nullable = false)
    private Boolean triedToQuit;

    @Column(name = "reasons_cant_quit", columnDefinition = "TEXT")
    private String reasonsCantQuit;

    @Column(name = "health_status", length = 255)
    private String healthStatus;

    @Column(name = "dependency_level", length = 50, nullable = false)
    private String dependencyLevel;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

}
