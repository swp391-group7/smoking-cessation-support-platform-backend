package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CesProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.UpdateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CesProgressRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
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
    @Override
    public CesProgressDto create(CreateCesProgressRequest request) {
        log.info("Creating new cessation progress for plan: {}", request.getPlanId());
// 1. Lấy plan và step từ DB
        Quit_Plan plan = quitPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new RuntimeException("Plan not found with ID: " + request.getPlanId()));
        Quit_Plan_Step step = quitPlanStepRepository.findById(request.getPlanStepId())
                .orElseThrow(() -> new RuntimeException("Plan step not found with ID: " + request.getPlanStepId()));

        try {
            // Tạo entity từ request
            Cessation_Progress progress = Cessation_Progress.builder()
                    .plan(plan)
                    .planStep(step)
                    .status(request.getStatus())
                    .mood(request.getMood())
                    .cigarettesSmoked(request.getCigarettesSmoked())
                    .note(request.getNote())
                    .logDate(LocalDateTime.now().toLocalDate())
                    .build();

            // Lưu vào database
            Cessation_Progress savedProgress = cesProgressRepository.save(progress);

            int streak = getConsecutiveZeroDays(plan.getId());
            plan.setCurrentZeroStreak(streak);
            if (streak > plan.getMaxZeroStreak()) plan.setMaxZeroStreak(streak);
            quitPlanRepository.save(plan);

            log.info("Successfully created cessation progress with ID: {}", savedProgress.getId());
            return mapToDto(savedProgress);

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
        // Lấy tất cả tiến độ của kế hoạch, chỉ quan tâm logDate và cigarettesSmoked
        List<Cessation_Progress> logs = cesProgressRepository
                .findByPlanIdOrderByLogDateDesc(planId);

        int count = 0;
        for (Cessation_Progress log : logs) {
            if (log.getLogDate().isEqual(today.minusDays(count))
                    && (log.getCigarettesSmoked() == null || log.getCigarettesSmoked() == 0)) {
                count++;
            } else {
                break;
            }
        }
        return count;
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
                .build();
    }
}