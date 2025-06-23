package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "fk_answer_question"))
    private Question question;

    @Column( name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;

    @Min(0)
    @Max(3)
    @Column(name = "point", nullable = false)
    private Integer point;

    @CreationTimestamp
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;
}
