package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
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
public class Coach {

    @Id  // Khóa chính đồng thời FK tới User
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)  // Mở rộng từ User
    @MapsId
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_coach_user"))
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;  // Tiểu sử huấn luyện viên

    @Column(length = 255)
    private String qualification;  // Trình độ chuyên môn

    @Column(name = "avg_rating", nullable = false,
            columnDefinition = "DECIMAL(2,1) DEFAULT 0 CHECK (avg_rating BETWEEN 0 AND 5)")
    private BigDecimal avgRating;  // Đánh giá trung bình 0-5 sao

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;  // Thời điểm tạo hồ sơ

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        if (this.avgRating == null) this.avgRating = BigDecimal.ZERO;
    }
}
