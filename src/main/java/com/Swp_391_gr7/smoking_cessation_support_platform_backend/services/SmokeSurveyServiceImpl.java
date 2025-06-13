package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Smoke_Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SmokeSurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSmokeSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SmokeSurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional  // Đảm bảo load + update/insert đều trong cùng transaction
public class SmokeSurveyServiceImpl implements SmokeSurveyService {

    private final SmokeSurveyRepository repository;
    private final UserRepository userRepository;

    @Override
    public SmokeSurveyDto createSurvey(UUID userId, CreateSmokeSurveyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));

        Smoke_Survey entity = Smoke_Survey.builder()
                // Không set .id(...) để JPA tự generate qua @GeneratedValue
                .user(user)
                .smokeDuration(request.getSmokeDuration())
                .cigarettesPerDay(request.getCigarettesPerDay())
                .priceEach(request.getPriceEach())
                .triedToQuit(request.getTriedToQuit())
                .reasonsCantQuit(request.getReasonsCantQuit())
                .healthStatus(request.getHealthStatus())
                .dependencyLevel(request.getDependencyLevel())
                .note(request.getNote())
                // Không set createAt, Hibernate tự xử lý @CreationTimestamp
                .build();
        Smoke_Survey saved = repository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public SmokeSurveyDto getSurvey(UUID userId) {
        Smoke_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));
        return mapToDto(entity);
    }

    @Override
    public SmokeSurveyDto updateSurvey(UUID userId, UpdateSmokeSurveyRequest request) {
        Smoke_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));
        // Update từng trường
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
        entity.setReasonsCantQuit(request.getReasonsCantQuit());
        entity.setHealthStatus(request.getHealthStatus());
        entity.setDependencyLevel(request.getDependencyLevel());
        entity.setNote(request.getNote());

        // Vì @Transactional, Hibernate tự flush update trước khi commit
        Smoke_Survey updated = repository.save(entity);
        return mapToDto(updated);
    }

    @Override
    public void deleteSurvey(UUID userId) {
        Smoke_Survey entity = repository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Survey not found for userId: " + userId
                ));
        repository.delete(entity);
    }

    public SmokeSurveyDto getSurveyById(UUID surveyId) {
        Smoke_Survey entity = repository.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for id: " + surveyId
                ));
        return mapToDto(entity);
    }

    private SmokeSurveyDto mapToDto(Smoke_Survey entity) {
        return SmokeSurveyDto.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .smokeDuration(entity.getSmokeDuration())
                .cigarettesPerDay(entity.getCigarettesPerDay())
                .priceEach(entity.getPriceEach())
                .triedToQuit(entity.getTriedToQuit())
                .reasonsCantQuit(entity.getReasonsCantQuit())
                .healthStatus(entity.getHealthStatus())
                .dependencyLevel(entity.getDependencyLevel())
                .note(entity.getNote())
                .createAt(entity.getCreateAt())
                .build();
    }
}




//testing commit
