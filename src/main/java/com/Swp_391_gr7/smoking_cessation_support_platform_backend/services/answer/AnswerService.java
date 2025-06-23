package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.answer;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.AnswerDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.CreateAnswerRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.answer.UpdateAnswerRequest;

import java.util.List;
import java.util.UUID;

public interface AnswerService {

    // create a new answer for a question
    AnswerDto createAnswer(UUID questionId, CreateAnswerRequest request);

    // update an existing answer by its ID
    AnswerDto updateAnswer(UUID answerId, UpdateAnswerRequest request);

    // get an answer by its ID
    AnswerDto getAnswerById(UUID answerId);

    // delete an answer by its ID
    void deleteAnswer(UUID answerId);

    // list all answers for a question
    List<AnswerDto> getAnswersByQuestionId(UUID questionId);
}
