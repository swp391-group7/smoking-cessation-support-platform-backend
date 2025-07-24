// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/services/feedBack/FeedBackServiceImpl.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.feedBack;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.CoachFeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackRequestDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.FeedbackResponseDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.feedBack.SystemFeedbackUpdateDTO;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CoachRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.FeedbackRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MembershipPackageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedbackRepository feedbackRepo;          // repo của entity FeedBack
    private final UserRepository userRepo;
    private final MembershipPackageRepository membershipRepo;
    private final CoachRepository coachRepo;

    private void recalcAndSaveAvgRating(UUID coachUserId) {
        // 1) Lấy tất cả feedback rating cho coach này
        var list = feedbackRepo.findByMembershipPackage_Coach_UserId(coachUserId);
        if (list.isEmpty()) {
            return;
        }

        // 2) Tính trung bình
        double avg = list.stream()
                .mapToInt(f -> f.getRating().intValue())
                .average()
                .orElse(0.0);

        // 3) Lấy coach entity, set và save
        Coach coach = coachRepo.findById(coachUserId)
                .orElseThrow(() -> new RuntimeException("Coach not found: " + coachUserId));
        coach.setAvgRating(BigDecimal.valueOf(avg));
        coachRepo.save(coach);
    }

    @Override
    public FeedbackResponseDTO createFeedBack(FeedbackRequestDTO dto) {
        // 1) Lấy userId từ JWT nếu cần
        UUID userId = dto.getUserId();

        if (dto.getTargetType() == FeedbackTarget.SYSTEM) {
            // nếu đã có feedback hệ thống => lỗi
            boolean exists = feedbackRepo
                    .existsByUser_IdAndTargetType(userId, FeedbackTarget.SYSTEM);
            if (exists) {
                throw new IllegalStateException(
                        "Bạn chỉ được feedback hệ thống 1 lần, hãy gọi API update thay vì create");
            }
        } else { // COACH
            // nếu đã có feedback cho same membershipPkg => lỗi
            boolean exists = feedbackRepo
                    .existsByUser_IdAndMembershipPackage_Id(
                            userId, dto.getMembershipPkgId());
            if (exists) {
                throw new IllegalStateException(
                        "Bạn đã feedback coach cho gói này rồi, hãy gọi updateFeedback");
            }
        }
        FeedBack fb = mapToEntity(dto, new FeedBack());
        FeedBack saved = feedbackRepo.save(fb);
        if (dto.getTargetType() == FeedbackTarget.COACH) {
            UUID coachId = fb.getMembershipPackage().getCoach().getUserId();
            recalcAndSaveAvgRating(coachId);
        }
        return mapToDto(saved);
    }

    @Override
    public FeedbackResponseDTO updateFeedBack(UUID id, FeedbackRequestDTO dto) {
        FeedBack fb = feedbackRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("FeedBack not found: " + id));
        fb = mapToEntity(dto, fb);
        FeedBack updated = feedbackRepo.save(fb);

        // Nếu là feedback coach, cập nhật lại avgRating của coach
        if (dto.getTargetType() == FeedbackTarget.COACH) {
            UUID coachId = updated.getMembershipPackage().getCoach().getUserId();
            recalcAndSaveAvgRating(coachId);
        }

        return mapToDto(updated);
    }
    @Override
    public void deleteFeedBack(UUID id) {
        feedbackRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponseDTO getFeedBackById(UUID id) {
        return feedbackRepo.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("FeedBack not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getAllFeedBack() {
        return feedbackRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedBackByUser(UUID userId) {
        return feedbackRepo.findByUser_Id(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedBackByTargetType(String targetType) {
        FeedbackTarget ft = FeedbackTarget.valueOf(targetType.toUpperCase());
        return feedbackRepo.findByTargetType(ft).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponseDTO> getFeedBackByCoach(UUID coachId) {
        return feedbackRepo.findByMembershipPackage_Coach_UserId(coachId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- Helpers ---
    private FeedBack mapToEntity(FeedbackRequestDTO dto, FeedBack fb) {
        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));
        fb.setUser(user);
        fb.setTargetType(dto.getTargetType());

        if (dto.getTargetType() == FeedbackTarget.COACH) {
            Membership_Package mp = membershipRepo.findById(dto.getMembershipPkgId())
                    .orElseThrow(() -> new RuntimeException("Membership not found: " + dto.getMembershipPkgId()));
            fb.setMembershipPackage(mp);
        } else {
            fb.setMembershipPackage(null);
        }

        fb.setRating(dto.getRating());
        fb.setComment(dto.getComment());
        return fb;
    }

    @Override
    public FeedbackResponseDTO feedbackCoach(UUID userId, CoachFeedbackRequestDTO dto) {
        // 1. Lấy gói active mới nhất của user
        Membership_Package pkg = membershipRepo
                .findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(userId)
                .orElseThrow(() ->
                        new IllegalStateException("User chưa có gói membership active"));

        // 2. Phải có coach đã được gán
        if (pkg.getCoach() == null) {
            throw new IllegalStateException("Gói active chưa có coach, không thể feedback coach");
        }

        // 3. Chuẩn bị FeedbackRequestDTO
        FeedbackRequestDTO req = FeedbackRequestDTO.builder()
                .userId(userId)
                .targetType(FeedbackTarget.COACH)
                .membershipPkgId(pkg.getId())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        // 4. Tạo
        return createFeedBack(req);
    }


    @Override
    public BigDecimal createSystemFeedback(UUID userId, FeedbackRequestDTO dto) {
        dto.setUserId(userId);
        dto.setTargetType(FeedbackTarget.SYSTEM);

        // Kiểm tra create duy nhất
        if (feedbackRepo.existsByUser_IdAndTargetType(userId, FeedbackTarget.SYSTEM)) {
            throw new IllegalStateException("Chỉ được feedback hệ thống 1 lần, hãy gọi updateSystemFeedback");
        }
        mapToEntity(dto, new FeedBack());
        feedbackRepo.save(mapToEntity(dto, new FeedBack()));
        return computeSystemAvgRating();
    }

    @Override
    public FeedbackResponseDTO updateSystemFeedback(UUID userId, FeedbackRequestDTO dto) {
        FeedBack fb = feedbackRepo.findByUser_IdAndTargetType(userId, FeedbackTarget.SYSTEM)
                .orElseThrow(() -> new RuntimeException("Feedback hệ thống không tồn tại"));

        fb.setRating(dto.getRating());
        fb.setComment(dto.getComment());

        FeedBack updated = feedbackRepo.save(fb);
        return mapToDto(updated);
    }
    @Override
    public FeedbackResponseDTO updateCoachFeedback(UUID userId, FeedbackRequestDTO dto) {
        Membership_Package pkg = membershipRepo
                .findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("User chưa có gói active"));

        FeedBack fb = feedbackRepo.findByUser_IdAndMembershipPackage_Id(userId, pkg.getId())
                .orElseThrow(() -> new RuntimeException("Feedback coach không tồn tại"));

        fb.setRating(dto.getRating());
        fb.setComment(dto.getComment());

        FeedBack updated = feedbackRepo.save(fb);
        recalcAndSaveAvgRating(pkg.getCoach().getUserId());
        return mapToDto(updated);
    }


    @Override
    @Transactional(readOnly = true)
    public BigDecimal computeSystemAvgRating() {
        double avg = feedbackRepo
                .findByTargetType(FeedbackTarget.SYSTEM)
                .stream()
                // nếu getRating() là Short hoặc Integer, ép về double
                .mapToDouble(f -> f.getRating().doubleValue())
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(avg);
    }


    private FeedbackResponseDTO mapToDto(FeedBack fb) {
        return FeedbackResponseDTO.builder()
                .id(fb.getId())
                .userId(fb.getUser().getId())
                .targetType(fb.getTargetType())
                .membershipPkgId(
                        fb.getMembershipPackage() != null ? fb.getMembershipPackage().getId() : null)
                .rating(fb.getRating())
                .comment(fb.getComment())
                .createdAt(fb.getCreatedAt())
                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public FeedbackResponseDTO getSystemFeedbackByUser(UUID userId) {
        FeedBack fb = feedbackRepo
                .findByUser_IdAndTargetType(userId, FeedbackTarget.SYSTEM)
                .orElseThrow(() ->
                        new RuntimeException("Feedback hệ thống không tồn tại cho user " + userId)
                );
        return mapToDto(fb);
    }

    @Override
    @Transactional
    public FeedbackResponseDTO updateSystemFeedbackById(UUID feedbackId, SystemFeedbackUpdateDTO dto) {
        FeedBack fb = feedbackRepo.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found: " + feedbackId));

        if (fb.getTargetType() != FeedbackTarget.SYSTEM) {
            throw new IllegalStateException("Cannot update non‑SYSTEM feedback via this endpoint");
        }

        fb.setRating(dto.getRating());
        fb.setComment(dto.getComment());
        FeedBack updated = feedbackRepo.save(fb);

        return mapToDto(updated);
    }
}
