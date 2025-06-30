package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.survey;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.AnswerDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.survey.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Answer;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Question;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Survey;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.AnswerRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuestionRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.SurveyRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

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
    public void deleteSurvey(UUID surveyId) {
        Survey entity = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey not found with Id: " + surveyId
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
    @Override
    public SurveyDetailDto getSurveyDetail(UUID surveyId) {
        // 1. Lấy Survey
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Survey không tồn tại: " + surveyId
                ));

        // 2. Lấy tất cả câu hỏi
        List<Question> questions = questionRepository.findBySurveyId(surveyId);

        // 3. Với mỗi câu hỏi, lấy đáp án và map thành DTO
        List<QuestionWithAnswersDto> questionDtos = questions.stream().map(q -> {
            List<Answer> answers = answerRepository.findByQuestionId(q.getId());
            List<AnswerDto> answerDtos = answers.stream()
                    .map(a -> AnswerDto.builder()
                            .id(a.getId())
                            .answerText(a.getAnswerText())
                            .point(a.getPoint())
                            .createdAt(a.getCreateAt())
                            .build())
                    .toList();

            return QuestionWithAnswersDto.builder()
                    .id(q.getId())
                    .content(q.getContent())
                    .createAt(q.getCreateAt())
                    .answers(answerDtos)
                    .build();
        }).toList();

        // 4. Map Survey + danh sách questionDtos về SurveyDetailDto
        return SurveyDetailDto.builder()
                .id(survey.getId())
                .userId(survey.getUser().getId())
                .typeSurvey(survey.getTypeSurvey())
                .createAt(survey.getCreateAt())
                .questions(questionDtos)
                .build();
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
