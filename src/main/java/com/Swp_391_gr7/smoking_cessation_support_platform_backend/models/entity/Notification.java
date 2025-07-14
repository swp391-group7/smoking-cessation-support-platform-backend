package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id  // Khóa chính UUID
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_notification_user_id"))
    private UUID userId;

    @Column(nullable = false)
    private String title;   // Tiêu đề thông báo

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message; // Nội dung thông báo

    @Column(nullable = false)
    private String channel; // Kênh gửi (email, push)

    @Column(name = "sent_at")
    @CreationTimestamp
    private LocalDateTime sentAt;        // Thời điểm gửi

    @Column(name = "expiration_at")
    private LocalDateTime expiredAt;  // Thời điểm hết hạn

    @Column(nullable = false)
    private String type;    // Loại thông báo (remind, badge)
}
