package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.coach;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.CoachDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.coach.UpdateCoachRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Coach;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CoachRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CoachServiceImpl implements CoachService {

    private final CoachRepository coachRepository;
    private final UserService userService;

    @Override
    @Transactional
    public CoachDto createCoach(UUID userId, CoachDto dto) {
        User user = userService.getUserEntityById(userId);

        // Chỉ tạo coach nếu role là COACH
        if (!"coach".equalsIgnoreCase(user.getRole().getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Chỉ người dùng với role COACH mới được tạo Coach");
        }

        // Check đã tồn tại coach chưa
        if (coachRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Coach đã tồn tại");
        }

        Coach coach = Coach.builder()
                .user(user) // @MapsId sẽ dùng user.id làm coach.id
                .bio(dto.getBio())
                .qualification(dto.getQualification())
                .build();

        Coach saved = coachRepository.save(coach);
        return mapToDto(saved);
    }

    @Override
    @Transactional
    public CoachDto updateCoach(UUID userId, UpdateCoachRequest request) {
        Coach coach = coachRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach không tồn tại"));

        coach.setBio(request.getBio());
        coach.setQualification(request.getQualification());


        return mapToDto(coachRepository.save(coach));
    }

    @Override
    @Transactional
    public void deleteCoach(UUID userId) {
        if (!coachRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach không tồn tại");
        }
        coachRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public CoachDto getCoachById(UUID userId) {
        Coach coach = coachRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach không tồn tại"));
        return mapToDto(coach);
    }

    @Override
    @Transactional
    public List<CoachDto> getAllCoaches() {
        return coachRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private CoachDto mapToDto(Coach coach) {
        return CoachDto.builder()
                .userId(coach.getUser().getId())
                .bio(coach.getBio())
                .qualification(coach.getQualification())

                .build();
    }
}
