package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends JpaRepository<Answer, UUID> {
    // Tìm danh sách câu trả lời theo ID của câu hỏi
    List<Answer> findByQuestionId(UUID questionId);
    // Đếm số câu trả lời theo ID câu hỏi
    long countByQuestion_Id(UUID questionId);
    // Kiểm tra xem một câu trả lời có tồn tại cho một câu hỏi cụ thể
    boolean existsByAnswerTextAndQuestion_Id(String answerText, UUID questionId);


}
