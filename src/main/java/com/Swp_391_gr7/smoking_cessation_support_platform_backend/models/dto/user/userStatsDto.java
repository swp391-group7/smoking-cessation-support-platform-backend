package com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class userStatsDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyUserStats {
        private int month;
        private long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgeGroupStats {
        private String ageGroup;
        private long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenderStats {
        private String gender;
        private long count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatisticsResponse {
        private List<MonthlyUserStats> monthlyStats;
        private List<AgeGroupStats> ageGroupStats;
        private List<GenderStats> genderStats;
    }
}
