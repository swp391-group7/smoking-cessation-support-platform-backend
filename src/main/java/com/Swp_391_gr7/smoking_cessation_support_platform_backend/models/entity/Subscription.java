package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.UUID;

public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_smoke_survey_user"))
    private User user;

    @Column(name = "subscription_name", length = 100)
    private String SubscriptionName;

    @Column(name = "price", nullable = false)
    private long price;

    @Column(name = "duration")
    private Duration duration;

    @Column(name="activation_status")
    private Boolean status; // true: active, false: inactive
}
