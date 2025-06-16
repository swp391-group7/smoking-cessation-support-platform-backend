//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSmokeSurveyRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSmokeSurveyRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Smoke_Survey;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SmokeSurveyRepository;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.web.server.ResponseStatusException;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//import static org.testng.Assert.*;
//
//public class SmokeSurveyTest {
//
//    @InjectMocks
//    private SmokeSurveyServiceImpl service;
//
//    @Mock
//    private SmokeSurveyRepository surveyRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @BeforeMethod
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateSurveySuccess() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        CreateSmokeSurveyRequest req = new CreateSmokeSurveyRequest();
//        req.setSmokeDuration(3);
//        req.setCigarettesPerDay(15);
//        req.setPriceEach(new BigDecimal("5.00"));
//        req.setTriedToQuit(true);
//        req.setReasonsCantQuit("Stress");
//        req.setHealthStatus("Good");
//        req.setNote("Note");
//
//        User user = new User();
//        user.setId(userId);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//        Smoke_Survey saved = Smoke_Survey.builder()
//                .id(UUID.randomUUID())
//                .user(user)
//                .smokeDuration(3)
//                .cigarettesPerDay(15)
//                .priceEach(new BigDecimal("5.00"))
//                .triedToQuit(true)
//                .reasonsCantQuit("Stress")
//                .healthStatus("Good")
//                .dependencyLevel(3)
//                .note("Note")
//                .createAt(LocalDateTime.now())
//                .build();
//        when(surveyRepository.save(any(Smoke_Survey.class))).thenReturn(saved);
//
//        // When
//        SmokeSurveyDto dto = service.createSurvey(userId, req);
//
//        // Then
//        assertNotNull(dto);
//        assertEquals(dto.getUserId(), userId);
//        assertEquals(dto.getSmokeDuration(), Integer.valueOf(3));
//        assertEquals(dto.getCigarettesPerDay(), Integer.valueOf(15));
//        assertEquals(dto.getDependencyLevel(), Integer.valueOf(3));
//        verify(userRepository).findById(userId);
//        verify(surveyRepository).save(any(Smoke_Survey.class));
//    }
//
//    @Test(expectedExceptions = ResponseStatusException.class)
//    public void testCreateSurveyUserNotFound() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        CreateSmokeSurveyRequest req = new CreateSmokeSurveyRequest();
//        req.setSmokeDuration(1);
//        req.setCigarettesPerDay(1);
//        req.setPriceEach(BigDecimal.ZERO);
//        req.setTriedToQuit(false);
//        req.setReasonsCantQuit("");
//        req.setHealthStatus("");
//        req.setNote("");
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // When / Then
//        service.createSurvey(userId, req);
//    }
//
//    @Test
//    public void testGetSurveySuccess() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        User user = new User();
//        user.setId(userId);
//
//        Smoke_Survey entity = Smoke_Survey.builder()
//                .id(UUID.randomUUID())
//                .user(user)
//                .smokeDuration(2)
//                .cigarettesPerDay(5)
//                .priceEach(new BigDecimal("2.50"))
//                .triedToQuit(false)
//                .reasonsCantQuit("None")
//                .healthStatus("Fair")
//                .dependencyLevel(1)
//                .note("n/a")
//                .createAt(LocalDateTime.now())
//                .build();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
//
//        // When
//        SmokeSurveyDto dto = service.getSurvey(userId);
//
//        // Then
//        assertEquals(dto.getId(), entity.getId());
//        assertEquals(dto.getUserId(), userId);
//        assertEquals(dto.getDependencyLevel(), Integer.valueOf(1));
//        verify(surveyRepository).findByUserId(userId);
//    }
//
//    @Test(expectedExceptions = ResponseStatusException.class)
//    public void testGetSurveyNotFound() {
//        UUID userId = UUID.randomUUID();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.empty());
//        service.getSurvey(userId);
//    }
//
//    @Test
//    public void testUpdateSurveySuccess() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        User user = new User(); user.setId(userId);
//        Smoke_Survey existing = Smoke_Survey.builder()
//                .id(UUID.randomUUID())
//                .user(user)
//                .smokeDuration(1)
//                .cigarettesPerDay(10)
//                .priceEach(new BigDecimal("3.00"))
//                .triedToQuit(false)
//                .reasonsCantQuit("Work")
//                .healthStatus("Poor")
//                .dependencyLevel(2)
//                .note("old note")
//                .createAt(LocalDateTime.now())
//                .build();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.of(existing));
//
//        UpdateSmokeSurveyRequest req = new UpdateSmokeSurveyRequest();
//        req.setSmokeDuration(2);
//        req.setCigarettesPerDay(20);
//        req.setPriceEach(new BigDecimal("4.00"));
//        req.setTriedToQuit(true);
//        req.setReasonsCantQuit("Stress");
//        req.setHealthStatus("Good");
//        req.setDependencyLevel(4);
//        req.setNote("new note");
//
//        Smoke_Survey updated = Smoke_Survey.builder()
//                .id(existing.getId())
//                .user(user)
//                .smokeDuration(2)
//                .cigarettesPerDay(20)
//                .priceEach(new BigDecimal("4.00"))
//                .triedToQuit(true)
//                .reasonsCantQuit("Stress")
//                .healthStatus("Good")
//                .dependencyLevel(4)
//                .note("new note")
//                .createAt(existing.getCreateAt())
//                .build();
//        when(surveyRepository.save(existing)).thenReturn(updated);
//
//        // When
//        SmokeSurveyDto dto = service.updateSurvey(userId, req);
//
//        // Then
//        assertEquals(dto.getCigarettesPerDay(), Integer.valueOf(20));
//        assertEquals(dto.getDependencyLevel(), Integer.valueOf(4));
//        verify(surveyRepository).findByUserId(userId);
//        verify(surveyRepository).save(existing);
//    }
//
//    @Test(expectedExceptions = ResponseStatusException.class)
//    public void testUpdateSurveyNotFound() {
//        UUID userId = UUID.randomUUID();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.empty());
//        service.updateSurvey(userId, new UpdateSmokeSurveyRequest());
//    }
//
//    @Test
//    public void testDeleteSurveySuccess() {
//        // Given
//        UUID userId = UUID.randomUUID();
//        User user = new User(); user.setId(userId);
//        Smoke_Survey entity = Smoke_Survey.builder()
//                .id(UUID.randomUUID())
//                .user(user)
//                .build();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.of(entity));
//
//        // When
//        service.deleteSurvey(userId);
//
//        // Then
//        verify(surveyRepository).findByUserId(userId);
//        verify(surveyRepository).delete(entity);
//    }
//
//    @Test(expectedExceptions = ResponseStatusException.class)
//    public void testDeleteSurveyNotFound() {
//        UUID userId = UUID.randomUUID();
//        when(surveyRepository.findByUserId(userId)).thenReturn(Optional.empty());
//        service.deleteSurvey(userId);
//    }
//
//    @Test
//    public void testGetSurveyByIdSuccess() {
//        // Given
//        UUID surveyId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        User user = new User(); user.setId(userId);
//        Smoke_Survey entity = Smoke_Survey.builder()
//                .id(surveyId)
//                .user(user)
//                .smokeDuration(4)
//                .cigarettesPerDay(8)
//                .priceEach(new BigDecimal("3.50"))
//                .triedToQuit(false)
//                .reasonsCantQuit("Habit")
//                .healthStatus("Ok")
//                .dependencyLevel(2)
//                .note("sample")
//                .createAt(LocalDateTime.now())
//                .build();
//        when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(entity));
//
//        // When
//        SmokeSurveyDto dto = service.getSurveyById(surveyId);
//
//        // Then
//        assertEquals(dto.getId(), surveyId);
//        assertEquals(dto.getUserId(), userId);
//        assertEquals(dto.getSmokeDuration(), Integer.valueOf(4));
//        verify(surveyRepository).findById(surveyId);
//    }
//
//    @Test(expectedExceptions = ResponseStatusException.class)
//    public void testGetSurveyByIdNotFound() {
//        UUID surveyId = UUID.randomUUID();
//        when(surveyRepository.findById(surveyId)).thenReturn(Optional.empty());
//        service.getSurveyById(surveyId);
//    }
//}