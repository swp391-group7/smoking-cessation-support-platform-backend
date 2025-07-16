package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.CreateMembershipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.MembershipPackageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.UpdateMemberShipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.user.UserDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Coach;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Package_Types;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CoachRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MembershipPackageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PackageTypeRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipPackageServiceImpl implements MembershipPackageService {

    private final MembershipPackageRepository membershipPackageRepository;
    private final UserRepository userRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final CoachRepository coachRepository;
    @Override
    public MembershipPackageDto create(UUID userId, UUID packageTypeId, CreateMembershipPackageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Package_Types packageType = packageTypeRepository.findById(packageTypeId)
                .orElseThrow(() -> new RuntimeException("Package type not found"));

        Membership_Package entity = Membership_Package.builder()
                .user(user)
                .packageType(packageType)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(request.isActive())          // <— đúng getter của DTO
                .build();

        return toDto(membershipPackageRepository.save(entity));
    }

    @Override
    public MembershipPackageDto update(UUID id, UpdateMemberShipPackageRequest request) {
        Membership_Package entity = membershipPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membership package not found"));

        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setActive(request.isActive());     // <— đúng getter của DTO

        return toDto(membershipPackageRepository.save(entity));
    }

    @Override
    public void delete(UUID id) {
        if (!membershipPackageRepository.existsById(id)) {
            throw new RuntimeException("Membership package not found");
        }
        membershipPackageRepository.deleteById(id);
    }

    @Override
    public MembershipPackageDto getById(UUID id) {
        return membershipPackageRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Membership package not found"));
    }

    @Override
    public List<MembershipPackageDto> getAllByUser(UUID userId) {
        return membershipPackageRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MembershipPackageDto getLatestByUser(UUID userId) {
        return membershipPackageRepository
                .findFirstByUserIdOrderByCreateAtDesc(userId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("No packages found for user"));
    }

    @Override
    public MembershipPackageDto getActivePackageByUser(UUID userId) {
        return membershipPackageRepository
                .findFirstByUserIdAndIsActiveTrueOrderByCreateAtDesc(userId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("No active package found for user"));
    }
    @Override
    public List<UUID> getAllActiveUserIds() {
        // Gọi repository custom query để lấy userId duy nhất
        return membershipPackageRepository
                .findDistinctUserIdsWithActiveMembership(LocalDateTime.now());
    }
    @Override
    public boolean hasActivePackageByUser(UUID userId) {
        return membershipPackageRepository
                .findFirstByUserIdAndIsActiveTrueAndEndDateAfterOrderByEndDateDesc(
                        userId, LocalDateTime.now()
                )
                .isPresent();
    }
    @Override
    @Transactional
    public MembershipPackageDto assignCoach(UUID userId, UUID coachId) {
        // 1. Lấy gói active của user
        Membership_Package pkg = membershipPackageRepository
                .findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("No active membership for user " + userId));

        // 2. Lấy reference của Coach (hoặc validate tồn tại nếu cần)
        Coach coach = coachRepository
                .findById(coachId)
                .orElseThrow(() -> new EntityNotFoundException("Coach not found: " + coachId));

        // 3. Gán coach và lưu
        pkg.setCoach(coach);
        Membership_Package saved = membershipPackageRepository.save(pkg);

        // 4. Chuyển thành DTO và trả về
        return toDto(saved);
    }
    // Hàm tiện ích chuyển User → UserDto
    private UserDto toUserDto(User u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                // Không nên expose password thực, thường sẽ null hoặc bỏ qua
                .password(null)
                .email(u.getEmail())
                .providerId(u.getProviderId())
                .fullName(u.getFullName())
                .phoneNumber(u.getPhoneNumber())
                .dob(u.getDob())
                .sex(u.getSex())
                .avtarPath(u.getAvtarPath())
                .preStatus(u.getPreStatus())
                .createdAt(u.getCreatedAt())
                .build();
    }

    @Override
    public List<UserDto> getUsersByCoach(UUID coachId) {
        List<User> users = membershipPackageRepository.findDistinctUsersByCoachId(coachId);
        return users.stream()
                .map(this::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MembershipPackageDto> getMembershipsByUserAndCoach(UUID userId, UUID coachId) {
        return membershipPackageRepository
                .findAllByUserIdAndCoach_UserId(userId, coachId)
                .stream()
                .map(this::toDto)  // dùng lại toDto() cho MembershipPackageDto
                .collect(Collectors.toList());
    }

    private MembershipPackageDto toDto(Membership_Package e) {
        return MembershipPackageDto.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .packagetTypeId(e.getPackageType().getId())
                .coachId(e.getCoach() != null ? e.getCoach().getUserId() : null) // <— có thể null nếu chưa gán coach
                .packageTypeName(e.getPackageType().getName()) // <— lấy tên từ Package_Types
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .isActive(e.isActive())               // <— đúng getter của Entity
                .build();
    }
}
