package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "membership_package")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Membership_Package {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_membership_user"))
    private User user;

    // Coach đồng hành
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coach_id", nullable = true,
            foreignKey = @ForeignKey(name = "fk_membership_coach"))
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "package_type_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_membership_package_type"))
    private Package_Types packageType;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

}
