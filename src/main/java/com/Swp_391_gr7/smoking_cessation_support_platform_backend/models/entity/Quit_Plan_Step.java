package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "quit_plan_steps")
public class Quit_Plan_Step {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_quit_plan_step_plan"))
    private Quit_Plan plan;  // Kế hoạch theo dõi

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;  // Số thứ tự bước

    @Column(name = "step_start_date", nullable = false)
    private LocalDate stepStartDate;  // Ngày bắt đầu bước

    @Column(name = "step_end_date", nullable = false)
    private LocalDate stepEndDate;  // Ngày kết thúc bước

    @Column(name = "step_description", columnDefinition = "TEXT")
    private String stepDescription;  // Mô tả bước

    @Column(name = "step_status", length = 50)
    private String stepStatus;  // Trạng thái bước (ví dụ: "Đang thực hiện", "Hoàn thành", "Bỏ qua")

    @Column(name = "target_cigarettes_per_day", nullable = false)
    private Integer targetCigarettesPerDay;  // Mục tiêu số điếu thuốc mỗi ngày trong bước này

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;  // Thời điểm tạo
}
