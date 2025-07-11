package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "cessation_progress")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Cessation_Progress {
    @Id  // Khóa chính UUID
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)  // Kế hoạch theo dõi
    @JoinColumn(name = "plan_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_progress_plan"))
    private Quit_Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_step_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_progress_plan_step"))
    private Quit_Plan_Step planStep;



    @CreationTimestamp
    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;  // Ngày log

    @Column(name = "cigarettes_smoked")
    private Integer cigarettesSmoked;  // Số điếu hút

    @Column(columnDefinition = "TEXT")
    private String note;  // Ghi chú

    @Column(length = 50)
    private String mood;  // Tâm trạng

    @Column(length = 50)
    private String status;  // Trạng thái ngày


}
