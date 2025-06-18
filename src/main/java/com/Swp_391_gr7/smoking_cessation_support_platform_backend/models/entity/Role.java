package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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
        private UUID id;

        @Column(nullable = false, length = 50)
        //@ColumnDefault("Member")
        private String role;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_role_user"))
        private User user;
    }


