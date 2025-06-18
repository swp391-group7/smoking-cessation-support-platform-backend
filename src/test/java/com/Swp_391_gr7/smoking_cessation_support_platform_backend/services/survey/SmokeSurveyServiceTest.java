package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Smoke_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SmokeSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.testng.MockitoTestNGListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

@SpringBootTest
@Listeners(MockitoTestNGListener.class)
public class SmokeSurveyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SmokeSurveyRepository repository;

    @InjectMocks
    private SmokeSurveyServiceImpl surveyService;

    @DataProvider(name = "dependencyLevels")
    public Object[][] dependencyLevels() {
        return new Object[][] {
                {0, 0, 1},
                {0, 5, 1},
                {0, 6, 1},
                {2, 5, 1},
                {2, 10, 2},
                {6, 20, 3},
                {11, 30, 4},
                {12, 40, 5},
        };
    }

    @Test(dataProvider = "dependencyLevels")
    public void testComputeDependencyLevel(int years, int cigs, int expectedLevel) {
        int actual = surveyService.computeDependencyLevel(years, cigs);
        assertEquals(actual, expectedLevel,
                String.format("Expected level %d for %d years & %d cigs/day but got %d", expectedLevel, years, cigs, actual));
    }

    @Test(dependsOnMethods = { "testComputeDependencyLevel"})
    public void testCreateSurveySuccess() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        CreateSmokeSurveyRequest req = new CreateSmokeSurveyRequest();
        req.setSmokeDuration(5);
        req.setCigarettesPerDay(15);
        req.setPriceEach(new BigDecimal(10));
        req.setTriedToQuit(true);
        req.setReasonsCantQuit("Stress");
        req.setHealthStatus("Good");
        req.setNote("No notes");

        Smoke_Survey saved = Smoke_Survey.builder()
                .id(UUID.randomUUID())
                .user(user)
                .smokeDuration(5)
                .cigarettesPerDay(15)
                .priceEach(new BigDecimal(10))
                .triedToQuit(true)
                .reasonsCantQuit("Stress")
                .healthStatus("Good")
                .dependencyLevel(surveyService.computeDependencyLevel(5, 15))
                .note("No notes")
                .build();

        SmokeSurveyDto expectedDto = new SmokeSurveyDto();
        expectedDto.setId(saved.getId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.save(any(Smoke_Survey.class))).thenReturn(saved);

        SmokeSurveyDto result = surveyService.createSurvey(userId, req);

        assertEquals(result.getId(), expectedDto.getId());
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(repository).save(any(Smoke_Survey.class));
    }

    @Test
    public void testCreateSurveyUserNotFound() {
        UUID userId = UUID.randomUUID();
        CreateSmokeSurveyRequest req = new CreateSmokeSurveyRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> surveyService.createSurvey(userId, req));
        Mockito.verify(repository, Mockito.never()).save(any());
    }
}
