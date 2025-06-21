package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.CreateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UserSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.testng.MockitoTestNGListener;
import org.springframework.web.server.ResponseStatusException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;


@Listeners(MockitoTestNGListener.class)
public class SmokeSurveyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSurveyRepository repository;

    @InjectMocks
    private UserSurveyServiceImpl surveyService;

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
@DataProvider(name = "dependencyLevelsCsv")
public Iterator<Object[]> dependencyLevelsFromCsv() throws IOException {
    List<Object[]> data = new ArrayList<>();
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("dependency_levels.csv");
    if (inputStream == null) {
        throw new FileNotFoundException("File not found in classpath: dependency_levels.csv");
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

    String line;
    while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        int years = Integer.parseInt(parts[0].trim());
        int cigs = Integer.parseInt(parts[1].trim());
        int expected = Integer.parseInt(parts[2].trim());
        data.add(new Object[]{years, cigs, expected});
    }
    reader.close();
    return data.iterator();
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

        CreateUserSurveyRequest req = new CreateUserSurveyRequest();
        req.setSmokeDuration(5);
        req.setCigarettesPerDay(15);
        req.setPriceEach(new BigDecimal(10));
        req.setTriedToQuit(true);
        req.setReasonsCantQuit("Stress");
        req.setHealthStatus("Good");
        req.setNote("No notes");

        User_Survey saved = User_Survey.builder()
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


        UserSurveyDto expectedDto = new UserSurveyDto();
        expectedDto.setId(saved.getId());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.save(any(User_Survey.class))).thenReturn(saved);

        UserSurveyDto result = surveyService.createSurvey(userId, req);

        assertEquals(result.getId(), expectedDto.getId());
        Mockito.verify(userRepository).findById(userId);
        Mockito.verify(repository).save(any(User_Survey.class));
    }

    @Test
    public void testCreateSurveyUserNotFound() {
        UUID userId = UUID.randomUUID();
        CreateUserSurveyRequest req = new CreateUserSurveyRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> surveyService.createSurvey(userId, req));
        Mockito.verify(repository, Mockito.never()).save(any());
    }
}
