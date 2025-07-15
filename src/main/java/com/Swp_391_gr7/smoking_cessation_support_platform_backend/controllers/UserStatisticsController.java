package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.userStatsDto.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class UserStatisticsController {

    private final UserService userService;

    @GetMapping("/users/monthly/{year}")
    public ResponseEntity<List<MonthlyUserStats>> getUserStatsByMonth(@PathVariable int year) {
        List<MonthlyUserStats> stats = userService.getUserStatsByMonthInYear(year);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/age-groups")
    public ResponseEntity<List<AgeGroupStats>> getUserStatsByAgeGroup() {
        List<AgeGroupStats> stats = userService.getUserStatsByAgeGroup();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/gender")
    public ResponseEntity<List<GenderStats>> getUserStatsByGender() {
        List<GenderStats> stats = userService.getUserStatsByGender();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users/all/{year}")
    public ResponseEntity<UserStatisticsResponse> getAllUserStatistics(@PathVariable int year) {
        UserStatisticsResponse stats = userService.getAllUserStatistics(year);
        return ResponseEntity.ok(stats);
    }
}