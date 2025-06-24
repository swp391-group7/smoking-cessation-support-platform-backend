package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.CreateMembershipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.MembershipPackageDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.UpdateMemberShipPackageRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Package_Types;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MembershipPackageRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PackageTypeRepository;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipPackageServiceImpl implements MembershipPackageService {

    private final MembershipPackageRepository membershipPackageRepository;
    private final UserRepository userRepository;
    private final PackageTypeRepository packageTypeRepository;

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

    private MembershipPackageDto toDto(Membership_Package e) {
        return MembershipPackageDto.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .packagetTypeId(e.getPackageType().getId())
                .packageTypeName(e.getPackageType().getName()) // <— lấy tên từ Package_Types
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .isActive(e.isActive())               // <— đúng getter của Entity
                .build();
    }
}
