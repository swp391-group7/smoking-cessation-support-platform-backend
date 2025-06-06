package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "quit_plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Quit_Plan {
    @Id  // Khóa chính UUID
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)  // Coach hỗ trợ
    @JoinColumn(name = "coach_id", foreignKey = @ForeignKey(name = "fk_quitplan_coach"))
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)  // Survey gốc
    @JoinColumn(name = "smoke_survey_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_quitplan_smokesurvey"))
    private Smoke_Survey smokeSurvey;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  // Ngày bắt đầu

    @Column(name = "target_date")
    private LocalDate targetDate;  // Ngày mục tiêu

    @Column(length = 100)
    private String method;  // Phương pháp

    @Column(length = 50)
    private String status;  // Trạng thái

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;  // Thời điểm tạo

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }
}
