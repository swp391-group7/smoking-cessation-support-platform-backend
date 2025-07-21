package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.CreateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UpdateUserRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.userStatsDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Coach;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Role;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CoachRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.RoleRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.coach.CoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CoachRepository coachRepository;

    @Override
    public UserDto createUser(CreateUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Role role = roleRepository.findByRole(request.getRoleName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Role not found: " + request.getRoleName()
                ));

        User newUser = User.builder()
                .username(request.getUsername())
                .password(hashedPassword)
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .dob(request.getDob())
                .avtarPath(request.getAvtarPath())
                .sex(request.getSex())
                .role(role)
                .build();

        User savedUser = userRepository.save(newUser);
        // Nếu role là COACH, tự động tạo hồ sơ Coach
        // Nếu là coach, tự tạo hồ sơ coach mặc định
        if ("coach".equalsIgnoreCase(savedUser.getRole().getRole())) {
            Coach coach = Coach.builder()
                    .user(savedUser)
                    .bio("")
                    .qualification("")
                    .avgRating(BigDecimal.ZERO)
                    .build();
            coachRepository.save(coach);
        }
        return mapToDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
        return mapToDto(u);
    }

    @Override
    public UserDto updateUser(UUID userId, UpdateUserRequest req) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        if (req.getEmail() != null) u.setEmail(req.getEmail());
        if (req.getFullName() != null) u.setFullName(req.getFullName());
        if (req.getPhoneNumber() != null) u.setPhoneNumber(req.getPhoneNumber());
        if (req.getDob() != null) u.setDob(req.getDob());
        if (req.getAvtarPath() != null) u.setAvtarPath(req.getAvtarPath());
        if (req.getSex() != null) u.setSex(req.getSex());
        if (req.getRoleName() != null) {
            Role role = roleRepository.findByRole(req.getRoleName())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Role not found: " + req.getRoleName()
                    ));
            u.setRole(role);
        }

        User updated = userRepository.save(u);
        return mapToDto(updated);
    }

    @Override
    public void deleteUser(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
        userRepository.delete(u);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(String roleName) {
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Role not found: " + roleName
                ));
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));
    }

    private UserDto mapToDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .password(u.getPassword())
                .email(u.getEmail())
                .providerId(u.getProviderId())
                .fullName(u.getFullName())
                .phoneNumber(u.getPhoneNumber())
                .dob(u.getDob())
                .avtarPath(u.getAvtarPath())
                .sex( u.getSex())
                .preStatus(u.getPreStatus())
                .createdAt(u.getCreatedAt())
                .roleName(u.getRole() != null ? u.getRole().getRole() : null)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public List<userStatsDto.MonthlyUserStats> getUserStatsByMonthInYear(int year) {
        List<Object[]> results = userRepository.countUsersByMonthInYear(year);

        // Tạo map để đảm bảo tất cả 12 tháng đều có dữ liệu
        Map<Integer, Long> monthlyData = new HashMap<>();
        for (int i = 1; i <= 12; i++) {
            monthlyData.put(i, 0L);
        }

        // Cập nhật dữ liệu từ database
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];
            monthlyData.put(month, count);
        }

        // Chuyển đổi thành danh sách MonthlyUserStats
        return monthlyData.entrySet().stream()
                .map(entry -> userStatsDto.MonthlyUserStats.builder()
                        .month(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .sorted(Comparator.comparingInt(userStatsDto.MonthlyUserStats::getMonth))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<userStatsDto.AgeGroupStats> getUserStatsByAgeGroup() {
        List<User> usersWithDob = userRepository.findAllUsersWithDob();
        LocalDate today = LocalDate.now();

        // Đếm người dùng theo nhóm tuổi
        Map<String, Long> ageGroupCounts = new HashMap<>();
        ageGroupCounts.put("18-29", 0L); // Tuổi trẻ - thường bắt đầu hút thuốc
        ageGroupCounts.put("30-49", 0L); // Tuổi trung niên - nhóm chính cần cai thuốc
        ageGroupCounts.put("50+", 0L);   // Tuổi cao - có nhiều vấn đề sức khỏe

        for (User user : usersWithDob) {
            int age = Period.between(user.getDob(), today).getYears();

            if (age >= 18 && age <= 29) {
                ageGroupCounts.put("18-29", ageGroupCounts.get("18-29") + 1);
            } else if (age >= 30 && age <= 49) {
                ageGroupCounts.put("30-49", ageGroupCounts.get("30-49") + 1);
            } else if (age >= 50) {
                ageGroupCounts.put("50+", ageGroupCounts.get("50+") + 1);
            }
        }

        return ageGroupCounts.entrySet().stream()
                .map(entry -> userStatsDto.AgeGroupStats.builder()
                        .ageGroup(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<userStatsDto.GenderStats> getUserStatsByGender() {
        List<Object[]> results = userRepository.countUsersByGender();

        // Đảm bảo cả male và female đều có dữ liệu
        Map<String, Long> genderData = new HashMap<>();
        genderData.put("male", 0L);
        genderData.put("female", 0L);

        for (Object[] result : results) {
            String gender = (String) result[0];
            Long count = (Long) result[1];
            if (gender != null) {
                genderData.put(gender.toLowerCase(), count);
            }
        }

        return genderData.entrySet().stream()
                .map(entry -> userStatsDto.GenderStats.builder()
                        .gender(entry.getKey())
                        .count(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public userStatsDto.UserStatisticsResponse getAllUserStatistics(int year) {
        return userStatsDto.UserStatisticsResponse.builder()
                .monthlyStats(getUserStatsByMonthInYear(year))
                .ageGroupStats(getUserStatsByAgeGroup())
                .genderStats(getUserStatsByGender())
                .build();
    }

}
