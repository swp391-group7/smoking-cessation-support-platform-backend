package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "feedback",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_feedback_per_user_type_membership",
                columnNames = {"user_id", "target_type", "membership_pkg_id"}
        )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_feedback_user"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 10)
    private FeedbackTarget targetType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_pkg_id",
            foreignKey = @ForeignKey(name = "fk_feedback_membership"))
    private Membership_Package membershipPackage;

    @Column(nullable = false)
    private Short rating;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    private void validateConsistency() {
        if (targetType == FeedbackTarget.SYSTEM && membershipPackage != null) {
            throw new IllegalStateException("SYSTEM feedback không được liên kết membershipPackage");
        }
        if (targetType == FeedbackTarget.COACH && membershipPackage == null) {
            throw new IllegalStateException("COACH feedback phải có membershipPackage liên quan");
        }
    }
}
