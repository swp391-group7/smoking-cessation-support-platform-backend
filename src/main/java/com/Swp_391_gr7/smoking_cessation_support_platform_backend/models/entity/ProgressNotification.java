package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "progress_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @JoinColumn(name = "Quit_plan_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_plan_id"))
    private UUID planId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message; // Nội dung thông báo

    @Column(nullable = false)
    private String channel; // Kênh gửi (email, push)

    @Column(name = "sent_at")
    @CreationTimestamp
    private LocalDateTime sentAt;

    @Column(name = "expiration_at")
    private LocalDateTime expirationAt;

    @Column(nullable = false)
    private String type;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;
}

