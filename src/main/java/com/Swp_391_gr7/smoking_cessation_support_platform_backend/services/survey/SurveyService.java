package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.CreateSurveyRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDetailDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.SurveyDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.UpdateSurveyRequest;

import java.util.List;
import java.util.UUID;

public interface SurveyService {
    SurveyDto createSurvey(UUID userId, CreateSurveyRequest request);
    SurveyDto getSurvey(UUID userId);

    SurveyDto updateSurvey(UUID userId, UpdateSurveyRequest request);

    void deleteSurvey(UUID surveyId);
    SurveyDto getSurveyById(UUID surveyId);
    SurveyDetailDto getSurveyDetail(UUID surveyId);



}
