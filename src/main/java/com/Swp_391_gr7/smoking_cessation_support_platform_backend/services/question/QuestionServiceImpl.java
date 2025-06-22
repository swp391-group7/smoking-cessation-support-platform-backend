package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.question;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.CreateQuestionRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.QuestionDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.question.UpdateQuestionRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Question;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuestionRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    @Override
    public QuestionDto createQuestion(UUID surveyId, CreateQuestionRequest request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        Question question = Question.builder()
                .survey(survey)
                .content(request.getContent())
                .build();

        Question saved = questionRepository.save(question);
        return toDto(saved);
    }

    @Override
    public QuestionDto updateQuestion(UUID questionId, UpdateQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        question.setContent(request.getContent());
        Question updated = questionRepository.save(question);

        return toDto(updated);
    }

    @Override
    public void deleteQuestion(UUID questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new IllegalArgumentException("Question not found");
        }
        questionRepository.deleteById(questionId);
    }

    @Override
    public QuestionDto getQuestionById(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        return toDto(question);
    }

    @Override
    public List<QuestionDto> getQuestionsBySurveyId(UUID surveyId) {
        return questionRepository.findBySurvey_IdOrderByCreateAtDesc(surveyId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private QuestionDto toDto(Question question) {
        return QuestionDto.builder()
                .id(question.getId())
                .surveyId(question.getSurvey().getId())
                .content(question.getContent())
                .createdAt(question.getCreateAt())
                .build();
    }
}
