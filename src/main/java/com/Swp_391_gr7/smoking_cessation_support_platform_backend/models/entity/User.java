package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.dialect.SpannerSqlAstTranslator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER) // hoặc LAZY tùy bạn
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String fullName;
    private String phoneNumber;
    private LocalDate dob;
    private String avtarPath;
    private String providerId;
    private String sex;
//    @ColumnDefault("false")
    private Boolean PreStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
