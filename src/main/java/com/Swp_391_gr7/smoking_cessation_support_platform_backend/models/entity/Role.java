package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;


    @Entity
    @Table(name = "role")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class Role {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
        private UUID id;                                   // Khóa chính UUID

        @Column(nullable = false, length = 50)
        private String role;                               // Tên vai trò (Guest, Member, Coach, Admin)

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_role_user"))
        private User user;                                // Người dùng có vai trò này
    }


