package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.quitPlan;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CesProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateCesProgressRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cesProgress.CreateProgressResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Quit_Plan_Step;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CesProgressRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuitPlanStepRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cesProgress.CesProgressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuitPlanServiceImplTests {

    @InjectMocks
    private CesProgressServiceImpl cesProgressService;

    @Mock
    private CesProgressRepository cesProgressRepository;

    @Mock
    private QuitPlanRepository quitPlanRepository;

    @Mock
    private QuitPlanStepRepository quitPlanStepRepository;

    private UUID planId = UUID.randomUUID();
    private UUID stepId = UUID.randomUUID();
    private Quit_Plan plan;
    private Quit_Plan_Step step;

    @BeforeEach
    void setUp() {
        plan = Quit_Plan.builder()
                .id(planId)
                .method("GRADUAL")
                .startDate(LocalDate.now().minusDays(5))
                .currentZeroStreak(0)
                .maxZeroStreak(0)
                .build();

        step = Quit_Plan_Step.builder()
                .id(stepId)
                .stepStartDate(LocalDate.now().minusDays(10))
                .stepEndDate(LocalDate.now().plusDays(10))
                .targetCigarettesPerDay(2)
                .build();
    }

    @Test
    void testGradualCase_2DaysUnderTarget_1DayOver() {
        // GIVEN
        CreateCesProgressRequest request = CreateCesProgressRequest.builder()
                .planId(planId)
                .planStepId(stepId)
                .status("OK")
                .mood("CALM")
                .cigarettesSmoked(0)
                .note("Case 1")
                .build();

        whenCommonMocking(request);

        List<CesProgressRepository.DailyTotal> mockData = List.of(
                new MockDailyTotal(LocalDate.now(), 1),      // dưới target
                new MockDailyTotal(LocalDate.now().minusDays(1), 2), // đúng target
                new MockDailyTotal(LocalDate.now().minusDays(2), 5)  // vượt target
        );

        when(cesProgressRepository.findDailyTotalsByPlan(planId)).thenReturn(mockData);
        when(quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId)).thenReturn(List.of(step));

        CreateProgressResponse result = cesProgressService.create(request);

        // THEN
        assertEquals(2, plan.getCurrentZeroStreak());
        assertEquals(2, plan.getMaxZeroStreak());
    }

    @Test
    void testGradualCase_AllPass_ShouldFullStreak() {
        CreateCesProgressRequest request = CreateCesProgressRequest.builder()
                .planId(planId)
                .planStepId(stepId)
                .status("OK")
                .mood("HAPPY")
                .cigarettesSmoked(0)
                .note("Case 2")
                .build();

        whenCommonMocking(request);

        List<CesProgressRepository.DailyTotal> mockData = IntStream.range(0, 5)
                .mapToObj(i -> new MockDailyTotal(LocalDate.now().minusDays(i), 1)) // luôn < target
                .collect(Collectors.toList());

        when(cesProgressRepository.findDailyTotalsByPlan(planId)).thenReturn(mockData);
        when(quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId)).thenReturn(List.of(step));

        CreateProgressResponse result = cesProgressService.create(request);

        assertEquals(5, plan.getCurrentZeroStreak());
        assertEquals(5, plan.getMaxZeroStreak());
    }

    @Test
    void testGradualCase_OneDayOver_ShouldResetStreak() {
        CreateCesProgressRequest request = CreateCesProgressRequest.builder()
                .planId(planId)
                .planStepId(stepId)
                .status("OK")
                .mood("ANGRY")
                .cigarettesSmoked(4)
                .note("Case 3")
                .build();

        whenCommonMocking(request);

        List<CesProgressRepository.DailyTotal> mockData = List.of(
                new MockDailyTotal(LocalDate.now(), 4) // vượt target
        );

        when(cesProgressRepository.findDailyTotalsByPlan(planId)).thenReturn(mockData);
        when(quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId)).thenReturn(List.of(step));

        CreateProgressResponse result = cesProgressService.create(request);

        assertEquals(0, plan.getCurrentZeroStreak());
        assertEquals(0, plan.getMaxZeroStreak());
    }

    @Test
    void testGradualCase_InterruptedStreak() {
        CreateCesProgressRequest request = CreateCesProgressRequest.builder()
                .planId(planId)
                .planStepId(stepId)
                .status("OK")
                .mood("OK")
                .cigarettesSmoked(0)
                .note("Case 4")
                .build();

        whenCommonMocking(request);

        List<CesProgressRepository.DailyTotal> mockData = List.of(
                new MockDailyTotal(LocalDate.now(), 1),
                new MockDailyTotal(LocalDate.now().minusDays(1), 1),
                new MockDailyTotal(LocalDate.now().minusDays(2), 5),  // reset
                new MockDailyTotal(LocalDate.now().minusDays(3), 0),
                new MockDailyTotal(LocalDate.now().minusDays(4), 0)
        );

        when(cesProgressRepository.findDailyTotalsByPlan(planId)).thenReturn(mockData);
        when(quitPlanStepRepository.findByPlanIdOrderByStepStartDateAsc(planId)).thenReturn(List.of(step));

        CreateProgressResponse result = cesProgressService.create(request);

        assertEquals(2, plan.getCurrentZeroStreak()); // từ ngày 0 -> ngày -1
        assertEquals(2, plan.getMaxZeroStreak());
    }

    // ========== COMMON MOCK SETUP ==========
    private void whenCommonMocking(CreateCesProgressRequest request) {
        when(quitPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(quitPlanStepRepository.findById(stepId)).thenReturn(Optional.of(step));
        when(cesProgressRepository.save(any())).thenAnswer(invocation -> {
            Cessation_Progress p = invocation.getArgument(0);
            p.setId(UUID.randomUUID());
            return p;
        });
    }

    // ========== MOCK IMPLEMENTATION ==========
    private static class MockDailyTotal implements CesProgressRepository.DailyTotal {
        private final LocalDate logDate;
        private final int totalCigarettes;

        public MockDailyTotal(LocalDate logDate, int totalCigarettes) {
            this.logDate = logDate;
            this.totalCigarettes = totalCigarettes;
        }

        @Override
        public LocalDate getLogDate() {
            return logDate;
        }

        @Override
        public Integer getTotalCigarettes() {
            return totalCigarettes;
        }
    }
}
