package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SurveyRepository;
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
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    @Override
    public SurveyDto createSurvey(UUID userId, CreateSurveyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found: " + userId
                ));

        Survey entity = Survey.builder()
                .user(user)
                .typeSurvey(request.getType_survey())
                .createAt(request.getCreateAt())
                .build();

        Survey saved = surveyRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public SurveyDto getSurvey(UUID userId) {
        Survey entity = surveyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for userId: " + userId
                ));
        return mapToDto(entity);
    }

    @Override
    public SurveyDto updateSurvey(UUID userId, UpdateSurveyRequest request) {
        Survey entity = surveyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for userId: " + userId
                ));

        if (request.getType_survey() != null) {
            entity.setTypeSurvey(request.getType_survey());
        }

        Survey updated = surveyRepository.save(entity);
        return mapToDto(updated);
    }

    @Override
    public void deleteSurvey(UUID userId) {
        Survey entity = surveyRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for userId: " + userId
                ));
        surveyRepository.delete(entity);
    }

    @Override
    public SurveyDto getSurveyById(UUID surveyId) {
        Survey entity = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found for id: " + surveyId
                ));
        return mapToDto(entity);
    }

    private SurveyDto mapToDto(Survey e) {
        return SurveyDto.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .type_survey(e.getTypeSurvey())
                .createAt(e.getCreateAt())
                .build();
    }
}
