package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.question;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.CreateQuestionRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.QuestionDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.UpdateQuestionRequest;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    QuestionDto createQuestion(UUID surveyId, CreateQuestionRequest request);
    QuestionDto updateQuestion(UUID questionId, UpdateQuestionRequest request);
    void deleteQuestion(UUID questionId);
    QuestionDto getQuestionById(UUID questionId);
    List<QuestionDto> getQuestionsBySurveyId(UUID surveyId);
}
