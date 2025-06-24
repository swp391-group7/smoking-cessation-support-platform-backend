package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.userSurvey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.CreateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UserSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.userSurvey.UpdateUserSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserSurveyServiceImpl implements UseSurveyService {

    private final UserSurveyRepository repository;
    private final UserRepository userRepository;

    @Override
    public UserSurveyDto createSurvey(UUID userId, CreateUserSurveyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));

        User_Survey entity = User_Survey.builder()
                .user(user)
                .smokeDuration(request.getSmokeDuration())
                .cigarettesPerDay(request.getCigarettesPerDay())
                .priceEach(request.getPriceEach())
                .triedToQuit(request.getTriedToQuit())
                .healthStatus(request.getHealthStatus())
                // set các câu trả lời a1..a8
                .a1(request.getA1())
                .a2(request.getA2())
                .a3(request.getA3())
                .a4(request.getA4())
                .a5(request.getA5())
                .a6(request.getA6())
                .a7(request.getA7())
                .a8(request.getA8())
                .dependencyLevel(request.getDependencyLevel())
                .note(request.getNote())
                .build();

        User_Survey saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public UserSurveyDto getSurvey(UUID userId) {
        User_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));
        return mapToDto(entity);
    }

    @Override
    public UserSurveyDto updateSurvey(UUID userId, UpdateUserSurveyRequest request) {
        User_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));

        // cập nhật các trường cơ bản
        entity.setSmokeDuration(request.getSmokeDuration());
        if (request.getCigarettesPerDay() != null) {
            entity.setCigarettesPerDay(request.getCigarettesPerDay());
        }
        if (request.getPriceEach() != null) {
            entity.setPriceEach(request.getPriceEach());
        }
        if (request.getTriedToQuit() != null) {
            entity.setTriedToQuit(request.getTriedToQuit());
        }
        entity.setHealthStatus(request.getHealthStatus());

        // cập nhật a1..a8 nếu có
        entity.setA1(request.getA1());
        entity.setA2(request.getA2());
        entity.setA3(request.getA3());
        entity.setA4(request.getA4());
        entity.setA5(request.getA5());
        entity.setA6(request.getA6());
        entity.setA7(request.getA7());
        entity.setA8(request.getA8());

        entity.setDependencyLevel(request.getDependencyLevel());
        entity.setNote(request.getNote());

        User_Survey updated = repository.save(entity);
        return mapToDto(updated);
    }

    @Override
    public void deleteSurvey(UUID userId) {
        User_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));
        repository.delete(entity);
    }
    @Override
    public UserSurveyDto getSurveyById(UUID surveyId) {
        User_Survey entity = repository.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for id: " + surveyId
                ));
        return mapToDto(entity);
    }


    @Override
    public UserSurveyDto getFirstSurveyOfUser(UUID userId) {
        User_Survey entity = repository.findFirstByUserIdOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No survey found for userId: " + userId
                ));
        return mapToDto(entity);
    }
    @Override
    public java.util.List<UserSurveyDto> getAllSurveyOfUser(UUID userId) {
        java.util.List<User_Survey> entities = repository.findAllByUserId(userId);
        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No surveys found for userId: " + userId);
        }
        return entities.stream().map(this::mapToDto).toList();
    }


    private UserSurveyDto mapToDto(User_Survey e) {
        return UserSurveyDto.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .smokeDuration(e.getSmokeDuration())
                .cigarettesPerDay(e.getCigarettesPerDay())
                .priceEach(e.getPriceEach())
                .triedToQuit(e.getTriedToQuit())
                .healthStatus(e.getHealthStatus())
                // map a1..a8
                .a1(e.getA1())
                .a2(e.getA2())
                .a3(e.getA3())
                .a4(e.getA4())
                .a5(e.getA5())
                .a6(e.getA6())
                .a7(e.getA7())
                .a8(e.getA8())
                .dependencyLevel(e.getDependencyLevel())
                .note(e.getNote())
                .createAt(e.getCreateAt())
                .build();
    }
}
