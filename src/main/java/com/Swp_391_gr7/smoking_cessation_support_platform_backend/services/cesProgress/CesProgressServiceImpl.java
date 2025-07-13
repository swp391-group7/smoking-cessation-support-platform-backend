package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.badge.BadgeDetailDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CesProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateProgressResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.UpdateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userbadge.UserBadgeDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userbadge.UserBadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CesProgressServiceImpl implements CesProgressService {

    private final CesProgressRepository cesProgressRepository;
    private final QuitPlanRepository quitPlanRepository;               // thêm
    private final QuitPlanStepRepository quitPlanStepRepository;
    private final UserSurveyRepository userSurveyRepository;

    private final UserBadgeService userBadgeService;    // mới
    private final BadgesRepository badgesRepository;     // để query badge theo condition
    @Override
    public CreateProgressResponse create(CreateCesProgressRequest request) {
        log.info("Creating new cessation progress for plan: {}", request.getPlanId());

        try {
            // 1. Lấy plan và step từ DB
            Quit_Plan plan = quitPlanRepository.findById(request.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + request.getPlanId()));
            Quit_Plan_Step step = quitPlanStepRepository.findById(request.getPlanStepId())
                    .orElseThrow(() -> new RuntimeException("Plan step not found with ID: " + request.getPlanStepId()));

            // 2. Tạo entity và lưu vào DB
            Cessation_Progress progress = Cessation_Progress.builder()
                    .plan(plan)
                    .planStep(step)
                    .status(request.getStatus())
                    .mood(request.getMood())
                    .cigarettesSmoked(request.getCigarettesSmoked())
                    .note(request.getNote())
                    .logDate(LocalDate.now())
                    .build();
            Cessation_Progress savedProgress = cesProgressRepository.save(progress);

            // 3. Tính currentStreak dựa trên method
            String method = plan.getMethod();
            int currentStreak;
            if ("IMMEDIATE".equalsIgnoreCase(method)) {
                currentStreak = calcZeroStreak(plan.getId());
            } else if ("GRADUAL".equalsIgnoreCase(method)) {
                currentStreak = calcGradualStreak(plan.getId());
            } else {
                log.warn("Unknown plan.method='{}', defaulting to zero-streak", method);
                currentStreak = calcZeroStreak(plan.getId());
            }

            // 4. Cập nhật plan
            plan.setCurrentZeroStreak(currentStreak);
            plan.setMaxZeroStreak(Math.max(plan.getMaxZeroStreak(), currentStreak));
            quitPlanRepository.save(plan);

            // 5. Lấy badge có condition == currentStreak và gán cho user
            UUID userId = plan.getUser().getId();
            List<Badges> toAward = badgesRepository.findAllByCondition(currentStreak);

            List<BadgeDetailDto> newBadges = new ArrayList<>();
            for (Badges badge : toAward) {
                UserBadgeDto ub = userBadgeService.assignBadge(userId, badge.getId());
                if (ub != null) {
                    newBadges.add(
                            BadgeDetailDto.builder()
                                    .id(badge.getId())
                                    .badgeName(badge.getBadgeName())
                                    .badgeDescription(badge.getBadgeDescription())
                                    .badgeImageUrl(badge.getBadgeImageUrl())
                                    .condition(badge.getCondition())
                                    .createdAt(badge.getCreatedAt())
                                    .build()
                    );
                }
            }

            log.info("Successfully created cessation progress with ID: {}", savedProgress.getId());
            // 6. Trả về cả progress và danh sách badge mới
            return new CreateProgressResponse(
                    mapToDto(savedProgress),
                    newBadges
            );

        } catch (Exception e) {
            log.error("Error creating cessation progress: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create cessation progress", e);
        }
    }

    @Override
    public CesProgressDto update(UpdateCesProgressRequest request) {
        log.info("Updating cessation progress with ID: {}", request.getId());

        try {
            // Tìm progress theo ID
            Cessation_Progress existingProgress = cesProgressRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Cessation progress not found with ID: " + request.getId()));

            // Cập nhật các field
            if (request.getStatus() != null) {
                existingProgress.setStatus(request.getStatus());
            }
            if (request.getMood() != null) {
                existingProgress.setMood(request.getMood());
            }
            if (request.getCigarettesSmoked() != null) {
                existingProgress.setCigarettesSmoked(request.getCigarettesSmoked());
            }
            if (request.getNote() != null) {
                existingProgress.setNote(request.getNote());
            }



            // Lưu thay đổi
            Cessation_Progress updatedProgress = cesProgressRepository.save(existingProgress);

            log.info("Successfully updated cessation progress with ID: {}", updatedProgress.getId());
            return mapToDto(updatedProgress);

        } catch (Exception e) {
            log.error("Error updating cessation progress: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update cessation progress", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CesProgressDto getById(UUID id) {
        log.info("Getting cessation progress by ID: {}", id);

        try {
            Cessation_Progress progress = cesProgressRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cessation progress not found with ID: " + id));

            log.info("Successfully retrieved cessation progress with ID: {}", id);
            return mapToDto(progress);

        } catch (Exception e) {
            log.error("Error getting cessation progress by ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to get cessation progress by ID", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CesProgressDto> getByPlanStepNumber(Integer stepNumber) {
        log.info("Getting cessation progress by plan step number: {}", stepNumber);

        try {
            List<Cessation_Progress> progressList = cesProgressRepository.findByPlanStepNumber(stepNumber);

            List<CesProgressDto> result = progressList.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            log.info("Successfully retrieved {} cessation progress records for step number: {}",
                    result.size(), stepNumber);
            return result;

        } catch (Exception e) {
            log.error("Error getting cessation progress by step number {}: {}", stepNumber, e.getMessage(), e);
            throw new RuntimeException("Failed to get cessation progress by step number", e);
        }
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting cessation progress with ID: {}", id);

        try {
            // Kiểm tra xem progress có tồn tại không
            if (!cesProgressRepository.existsById(id)) {
                throw new RuntimeException("Cessation progress not found with ID: " + id);
            }

            cesProgressRepository.deleteById(id);

            log.info("Successfully deleted cessation progress with ID: {}", id);

        } catch (Exception e) {
            log.error("Error deleting cessation progress with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete cessation progress", e);
        }
    }

    /**
     * Tính tổng số thuốc đã hút trong ngày hôm nay
     * @return tổng số thuốc đã hút
     */
    public Integer getTotalCigarettesToday() {
        return getTotalCigarettesByDate(LocalDate.now());
    }

    /**
     * Tính tổng số thuốc đã hút trong 1 ngày cụ thể
     * @param date ngày cần tính
     * @return tổng số thuốc đã hút
     */
    public Integer getTotalCigarettesByDate(LocalDate date) {
        log.info("Calculating total cigarettes smoked on date: {}", date);

        try {
            List<Cessation_Progress> progressList = cesProgressRepository.findByLogDate(date);

            int totalCigarettes = progressList.stream()
                    .mapToInt(progress -> progress.getCigarettesSmoked() != null ? progress.getCigarettesSmoked() : 0)
                    .sum();

            log.info("Total cigarettes smoked on {}: {} (from {} records)",
                    date, totalCigarettes, progressList.size());

            return totalCigarettes;

        } catch (Exception e) {
            log.error("Error calculating total cigarettes for date {}: {}", date, e.getMessage(), e);
            throw new RuntimeException("Failed to calculate total cigarettes", e);
        }
    }

    /**
     * Tính tổng số thuốc đã hút trong 1 tuần (7 ngày gần nhất)
     * @return tổng số thuốc đã hút trong tuần
     */
    public Integer getTotalCigarettesThisWeek() {
        log.info("Calculating total cigarettes smoked this week");

        try {
            LocalDate today = LocalDate.now();
            LocalDate weekAgo = today.minusDays(6); // 7 ngày bao gồm hôm nay

            int totalCigarettes = 0;

            // Tính tổng cho mỗi ngày trong tuần
            for (LocalDate date = weekAgo; !date.isAfter(today); date = date.plusDays(1)) {
                totalCigarettes += getTotalCigarettesByDate(date);
            }

            log.info("Total cigarettes smoked this week (from {} to {}): {}",
                    weekAgo, today, totalCigarettes);

            return totalCigarettes;

        } catch (Exception e) {
            log.error("Error calculating total cigarettes for this week: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to calculate weekly cigarettes", e);
        }
    }

    /**
     * Lấy chi tiết số thuốc đã hút theo từng ngày trong khoảng thời gian
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return Map với key là ngày, value là tổng số thuốc
     */
    public java.util.Map<LocalDate, Integer> getCigarettesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Getting cigarettes statistics from {} to {}", startDate, endDate);

        try {
            java.util.Map<LocalDate, Integer> result = new java.util.HashMap<>();

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                int totalForDate = getTotalCigarettesByDate(date);
                result.put(date, totalForDate);
            }

            log.info("Successfully calculated cigarettes statistics for {} days", result.size());
            return result;

        } catch (Exception e) {
            log.error("Error getting cigarettes statistics: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get cigarettes statistics", e);
        }
    }
    /**
     * @return số ngày kể từ ngày bắt đầu plan đến hôm nay (bao gồm cả hôm nay)
     */
    public Integer getDaysSinceQuit(UUID planId) {
        Quit_Plan plan = quitPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));
        LocalDate start = plan.getStartDate(); // giả sử Plan có trường startDate
        long days = ChronoUnit.DAYS.between(start, LocalDate.now()) + 1;
        return (int) days; // ép về Integer, an toàn nếu không quá 2^31-1 ngày
    }

    /**
     * @return số ngày liên tiếp gần nhất mà cigarettesSmoked == 0
     */
    public Integer getConsecutiveZeroDays(UUID planId) {
        LocalDate today = LocalDate.now();
        List<CesProgressRepository.DailyTotal> totals =
                cesProgressRepository.findDailyTotalsByPlan(planId);

        int streak = 0;
        for (CesProgressRepository.DailyTotal dt : totals) {
            // Nếu đã qua ngày liên tiếp (vd: ngày hôm nay − streak)
            if (!dt.getLogDate().isEqual(today.minusDays(streak))) {
                break;
            }
            // Nếu tổng thuốc > 0 thì chuỗi bị phá
            if (dt.getTotalCigarettes() > 0) {
                break;
            }
            streak++;
        }
        return streak;
    }
    private int calcZeroStreak(UUID planId) {
        LocalDate today = LocalDate.now();
        List<CesProgressRepository.DailyTotal> totals =
                cesProgressRepository.findDailyTotalsByPlan(planId);

        int streak = 0;
        for (var dt : totals) {
            if (!dt.getLogDate().isEqual(today.minusDays(streak)) || dt.getTotalCigarettes() > 0) {
                break;
            }
            streak++;
        }
        return streak;
    }
    /**
     * Tính chuỗi ngày đạt target (cho GRADUAL method)
     * @param planId ID của plan
     * @return số ngày liên tiếp đạt target theo từng step
     */
    private int calcGradualStreak(UUID planId) {
        Quit_Plan plan = quitPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));

        List<Quit_Plan_Step> steps =
                quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId);

        if (steps.isEmpty()) {
            return 0;
        }

        // LẤY DANH SÁCH PROGRESS RECORDS, KHÔNG PHẢI MAP NGÀY
        List<CesProgressRepository.DailyTotal> dailyTotals =
                cesProgressRepository.findDailyTotalsByPlan(planId);

        // Sắp xếp theo ngày giảm dần (mới nhất trước)
        dailyTotals.sort((a, b) -> b.getLogDate().compareTo(a.getLogDate()));

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        // Duyệt qua từng ngày có progress record
        for (CesProgressRepository.DailyTotal dailyTotal : dailyTotals) {
            LocalDate recordDate = dailyTotal.getLogDate();

            // Nếu ngày này không phải ngày mong đợi tiếp theo → PHÁ CHUỖI
            if (!recordDate.isEqual(expectedDate)) {
                break;
            }

            // Tìm step tương ứng với ngày này
            Quit_Plan_Step currentStep = null;
            for (Quit_Plan_Step step : steps) {
                if (!recordDate.isBefore(step.getStepStartDate()) &&
                        !recordDate.isAfter(step.getStepEndDate())) {
                    currentStep = step;
                    break;
                }
            }

            // Nếu không có step cho ngày này → PHÁ CHUỖI
            if (currentStep == null) {
                break;
            }

            // Kiểm tra có đạt target không
            int totalCigarettes = dailyTotal.getTotalCigarettes();
            if (totalCigarettes > currentStep.getTargetCigarettesPerDay()) {
                break; // PHÁ CHUỖI
            }

            // Đạt target → tăng streak
            streak++;
            expectedDate = expectedDate.minusDays(1);
        }

        return streak;
    }
    private Quit_Plan_Step findStepForDate(LocalDate date, List<Quit_Plan_Step> steps) {
        for (Quit_Plan_Step step : steps) {
            if (!date.isBefore(step.getStepStartDate()) &&
                    !date.isAfter(step.getStepEndDate())) {
                return step;
            }
        }
        return null;
    }
    private int calcGradualStreakFromLastRecord(UUID planId) {
        List<CesProgressRepository.DailyTotal> dailyTotals =
                cesProgressRepository.findDailyTotalsByPlan(planId);

        if (dailyTotals.isEmpty()) {
            return 0;
        }

        // Sắp xếp theo ngày giảm dần
        dailyTotals.sort((a, b) -> b.getLogDate().compareTo(a.getLogDate()));

        List<Quit_Plan_Step> steps =
                quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId);

        int streak = 0;
        LocalDate previousDate = null;

        for (CesProgressRepository.DailyTotal dailyTotal : dailyTotals) {
            LocalDate currentDate = dailyTotal.getLogDate();

            // Kiểm tra tính liên tiếp (trừ record đầu tiên)
            if (previousDate != null && !currentDate.isEqual(previousDate.minusDays(1))) {
                break; // Có gap → phá chuỗi
            }

            // Tìm step và kiểm tra target
            Quit_Plan_Step currentStep = findStepForDate(currentDate, steps);
            if (currentStep == null ||
                    dailyTotal.getTotalCigarettes() > currentStep.getTargetCigarettesPerDay()) {
                break;
            }

            streak++;
            previousDate = currentDate;
        }

        return streak;
    }

    public int getAvoidedCigarettes(UUID planId) {
        // 1. Lấy plan để có startDate
        Quit_Plan plan = quitPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));
        LocalDate startDate = plan.getStartDate();

        // 2. Lấy tất cả steps
        List<Quit_Plan_Step> steps =
                quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId);

        // 3. Lấy các bản ghi progress và tìm ngày cuối cùng
        List<CesProgressRepository.DailyTotal> dailyTotals =
                cesProgressRepository.findDailyTotalsByPlan(planId);
        LocalDate lastRecordDate = dailyTotals.stream()
                .map(CesProgressRepository.DailyTotal::getLogDate)
                .max(LocalDate::compareTo)
                .orElse(startDate);

        // 4. Chuyển list thành map ngày -> actual
        Map<LocalDate, Integer> actualByDate = dailyTotals.stream()
                .collect(Collectors.toMap(
                        CesProgressRepository.DailyTotal::getLogDate,
                        CesProgressRepository.DailyTotal::getTotalCigarettes
                ));

        int totalAvoided = 0;
        // 5. Duyệt từng ngày từ startDate đến lastRecordDate
        for (LocalDate curr = startDate; !curr.isAfter(lastRecordDate); curr = curr.plusDays(1)) {
            final LocalDate date = curr;   // phải là effectively final để dùng trong lambda
            int actual = actualByDate.getOrDefault(date, 0);

            // tìm đúng step chứa ngày này
            Quit_Plan_Step stepForDate = steps.stream()
                    .filter(s ->
                            !date.isBefore(s.getStepStartDate()) &&
                                    !date.isAfter(s.getStepEndDate())
                    )
                    .findFirst()
                    .orElse(null);

            if (stepForDate != null) {
                int target = stepForDate.getTargetCigarettesPerDay();
                int avoided = target - actual;
                if (avoided > 0) {
                    totalAvoided += avoided;
                }
            }
        }

        return totalAvoided;
    }


    public BigDecimal getMoneySaved(UUID planId) {
        int avoided = getAvoidedCigarettes(planId);
        if (avoided == 0) {
            return BigDecimal.ZERO;
        }

        Quit_Plan plan = quitPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found: " + planId));
        UUID userId = plan.getUser().getId();

        User_Survey survey = userSurveyRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User survey not found for user: " + userId));
        BigDecimal priceEach = survey.getPriceEach();

        return priceEach.multiply(BigDecimal.valueOf(avoided));
    }
    @Override
    @Transactional(readOnly = true)
    public List<CesProgressDto> getAllByPlanId(UUID planId) {
        // 1. Kiểm tra plan tồn tại
        if (!quitPlanRepository.existsById(planId)) {
            throw new RuntimeException("Plan not found: " + planId);
        }
        // 2. Lấy tất cả progress entity theo planId
        List<Cessation_Progress> progressList =
                cesProgressRepository.findAllByPlanIdOrderByLogDateAsc(planId);

        // 3. Map sang DTO
        return progressList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    /**
     * Đếm số ngày có bản ghi progress duy nhất của một plan
     * (không tính các bản ghi cùng ngày)
     */
    @Override
    @Transactional(readOnly = true)
    public int countUniqueProgress(UUID planId) {
        // 1. Lấy tất cả progress cho plan (sắp theo logDate)
        List<Cessation_Progress> all = cesProgressRepository.findAllByPlanIdOrderByLogDateAsc(planId);

        // 2. Dùng Set để giữ các ngày duy nhất
        long uniqueDays = all.stream()
                .map(Cessation_Progress::getLogDate)
                .distinct()
                .count();

        return (int) uniqueDays;
    }











    /**
     * Chuyển đổi entity sang DTO
     */
    private CesProgressDto mapToDto(Cessation_Progress progress) {
        return CesProgressDto.builder()
                .id(progress.getId())
                .planId(progress.getPlan() != null ? progress.getPlan().getId() : null)
                .planStepId(progress.getPlanStep() != null ? progress.getPlanStep().getId() : null)
                .status(progress.getStatus())
                .mood(progress.getMood())
                .cigarettesSmoked(progress.getCigarettesSmoked())
                .note(progress.getNote())
                .logDate(progress.getLogDate())
                .build();
    }
}