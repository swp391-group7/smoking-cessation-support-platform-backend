package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.packagetype;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType.*;

import java.util.List;
import java.util.UUID;

public interface PackageTypeService {
    PackageTypeDto createPackageType( CreatePackageTypeRequest request);
    PackageTypeDto updatePackageType(UUID id, UpdatePackageTypeRequest request);
    List<PackageTypeDto> getAllPackageTypes();
    PackageTypeDto getPackageTypeById(UUID id);
    void deletePackageType(UUID id);
}
