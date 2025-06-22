package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDto;

import java.util.UUID;

public interface SurveyService {
    SurveyDto createSurvey(UUID userId, CreateSurveyRequest request);
    SurveyDto getSurvey(UUID userId);
    SurveyDto updateSurvey(UUID userId, CreateSurveyRequest request);
    void deleteSurvey(UUID userId);
    SurveyDto getSurveyById(UUID surveyId);
}
