package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.userStatsDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    /**
     * Đăng ký (tạo mới) User
     */
    UserDto createUser(CreateUserRequest request);

    /**
     * Lấy thông tin user theo userId
     */
    UserDto getUserById(UUID userId);

    /**
     * Cập nhật thông tin user
     */
    UserDto updateUser(UUID userId, UpdateUserRequest request);

    /**
     * Xóa user
     */
    void deleteUser(UUID userId);

    List<UserDto> getUsersByRole(String roleName);

    List<UserDto> getAllUsers();

    User getUserEntityById(UUID userId);


    List<userStatsDto.MonthlyUserStats> getUserStatsByMonthInYear(int year);
    List<userStatsDto.AgeGroupStats> getUserStatsByAgeGroup();
    List<userStatsDto.GenderStats> getUserStatsByGender();
    userStatsDto.UserStatisticsResponse getAllUserStatistics(int year);
}
