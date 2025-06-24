package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.membershippackage;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.membershipPackage.*;
import java.util.List;
import java.util.UUID;

public interface MembershipPackageService {
    MembershipPackageDto createPackage(UUID userId, UUID packageId, CreateMembershipPackageRequest request);
    MembershipPackageDto updatePackage(UUID membershipId, UpdateMemberShipPackageRequest request);
    MembershipPackageDto getActivePackageByUserId(UUID userId);
    List<MembershipPackageDto> getAllPackagesByUserId(UUID userId);
}
