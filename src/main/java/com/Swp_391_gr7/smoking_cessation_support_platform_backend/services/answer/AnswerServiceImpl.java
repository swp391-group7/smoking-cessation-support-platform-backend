package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.answer;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.AnswerDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.CreateAnswerRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.UpdateAnswerRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Answer;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Question;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.AnswerRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    // create a new answer for a question
    @Override
    public AnswerDto createAnswer(UUID questionId, CreateAnswerRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        Answer answer = Answer.builder()
                .question(question)
                .answerText(request.getAnswerText())
                .point(request.getPoint())
                .build();

        Answer saved = answerRepository.save(answer);
        return toDto(saved);
    }

    // update an existing answer by its ID
    @Override
    public AnswerDto updateAnswer(UUID answerId, UpdateAnswerRequest request) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));

        answer.setAnswerText(request.getAnswerText());
        answer.setPoint(request.getPoint());

        Answer updated = answerRepository.save(answer);
        return toDto(updated);
    }

    // get an answer by its ID
    @Override
    public AnswerDto getAnswerById(UUID answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new IllegalArgumentException("Answer not found"));
        return toDto(answer);
    }

    // delete an answer by its ID
    @Override
    public void deleteAnswer(UUID answerId) {
        if (!answerRepository.existsById(answerId)) {
            throw new IllegalArgumentException("Answer not found");
        }
        answerRepository.deleteById(answerId);
    }

    // list all answers for a question
    @Override
    public List<AnswerDto> getAnswersByQuestionId(UUID questionId) {
        return answerRepository.findByQuestionId(questionId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // convert entity to DTO
    private AnswerDto toDto(Answer answer) {
        return AnswerDto.builder()
                .id(answer.getId())
                .questionId(answer.getQuestion().getId())
                .answerText(answer.getAnswerText())
                .createdAt(answer.getCreateAt())
                .point(answer.getPoint())
                .build();
    }
}
