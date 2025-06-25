package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coaches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Check(constraints = "avg_rating BETWEEN 0 AND 5")
public class Coach {

    @Id
    @Column(name = "user_id")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // dùng giá trị user.id làm khóa chính
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_coach_user"))
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 255)
    private String qualification;

    @Column(name = "avg_rating", nullable = false, precision = 2, scale = 1)
    @ColumnDefault("0.0")
    private BigDecimal avgRating;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
