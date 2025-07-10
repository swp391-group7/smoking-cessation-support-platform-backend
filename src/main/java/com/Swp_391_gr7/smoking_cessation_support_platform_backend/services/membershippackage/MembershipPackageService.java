package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.*;

import java.util.List;
import java.util.UUID;

public interface MembershipPackageService {

    MembershipPackageDto create(UUID userId, UUID packageTypeId, CreateMembershipPackageRequest request);

    MembershipPackageDto update(UUID id, UpdateMemberShipPackageRequest request);

    void delete(UUID id);

    MembershipPackageDto getById(UUID id);

    List<MembershipPackageDto> getAllByUser(UUID userId);

    MembershipPackageDto getLatestByUser(UUID userId);

    MembershipPackageDto getActivePackageByUser(UUID userId);
    List<UUID> getAllActiveUserIds();
    boolean hasActivePackageByUser(UUID userId);
}
