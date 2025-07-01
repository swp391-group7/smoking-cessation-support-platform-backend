package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.packagetype;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.packageType.*;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Package_Types;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.PackageTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PackageTypeServiceImpl implements PackageTypeService {

    private final PackageTypeRepository packageTypeRepository;

    @Override
    public PackageTypeDto createPackageType(CreatePackageTypeRequest request) {
        Package_Types entity = Package_Types.builder()
                .name(request.getName())
                .description(request.getDescription())
                .des1(request.getDes1())
                .des2(request.getDes2())
                .des3(request.getDes3())
                .des4(request.getDes4())
                .des5(request.getDes5())
                .price(request.getPrice())
                .duration(request.getDuration())
                .createAt(LocalDateTime.now())
                .build();

        return toDto(packageTypeRepository.save(entity));
    }

    @Override
    public PackageTypeDto updatePackageType(UUID id, UpdatePackageTypeRequest request) {
        Package_Types entity = packageTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package type not found"));

        entity.setDescription(request.getDescription());
        entity.setPrice(request.getPrice());
        entity.setDuration(request.getDuration());

        return toDto(packageTypeRepository.save(entity));
    }

    @Override
    public List<PackageTypeDto> getAllPackageTypes() {
        return packageTypeRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PackageTypeDto getPackageTypeById(UUID id) {
        return packageTypeRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Package type not found"));
    }

    @Override
    public void deletePackageType(UUID id) {
        if (!packageTypeRepository.existsById(id)) {
            throw new RuntimeException("Package type not found");
        }
        packageTypeRepository.deleteById(id);
    }

    private PackageTypeDto toDto(Package_Types entity) {
        return PackageTypeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .duration(entity.getDuration())
                .createAt(entity.getCreateAt())
                .build();
    }
}
