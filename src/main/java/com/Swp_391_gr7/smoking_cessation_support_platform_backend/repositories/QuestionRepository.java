package com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository

public interface QuestionRepository extends JpaRepository<Question, UUID> {

    // Lấy tất cả câu hỏi thuộc về một survey (dựa trên id survey)
    List<Question> findBySurveyId(UUID surveyId);

    // Đếm số câu hỏi trong một survey
    long countBySurvey_Id(UUID surveyId);

    // Lấy danh sách câu hỏi sắp xếp theo ngày tạo (mới nhất trước)
    List<Question> findBySurvey_IdOrderByCreateAtDesc(UUID surveyId);

    // Tìm câu hỏi theo nội dung (tùy chọn ignoreCase nếu cần)
    List<Question> findByContentContainingIgnoreCase(String keyword);

    // Kiểm tra xem một câu hỏi có tồn tại trong survey nào đó
    boolean existsByContentAndSurvey_Id(String content, UUID surveyId);
}
