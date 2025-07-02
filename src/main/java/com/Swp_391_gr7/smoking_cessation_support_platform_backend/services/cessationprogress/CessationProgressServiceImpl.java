package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.cessationprogress;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.cessationprogress.CessationProgressDto;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.CessationProgressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CessationProgressServiceImpl implements CessationProgressService {
    private final CessationProgressRepository repository;

    private CessationProgressDto mapToDto(Cessation_Progress entity) {
        return CessationProgressDto.builder()
                .id(entity.getId())
                .planId(entity.getPlan() != null ? entity.getPlan().getId() : null)
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .logDate(entity.getLogDate())
                .cigarettesSmoked(entity.getCigarettesSmoked())
                .note(entity.getNote())
                .mood(entity.getMood())
                .status(entity.getStatus())
                .build();
    }

    @Override
    public CessationProgressDto create(Cessation_Progress progress) {
        progress.setId(null);
        progress.setLogDate(LocalDate.now());
        return mapToDto(repository.save(progress));
    }

    @Override
    public List<CessationProgressDto> findAll() {
        return repository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public Optional<CessationProgressDto> findById(UUID id) {
        return repository.findById(id).map(this::mapToDto);
    }

    @Override
    public CessationProgressDto update(UUID id, Cessation_Progress progress) {
        Cessation_Progress existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Progress not found"));
        progress.setId(id);
        progress.setLogDate(existing.getLogDate());
        return mapToDto(repository.save(progress));
    }

    @Override
    public List<CessationProgressDto> findByUserId(UUID userId) {
        return repository.findByUser_Id(userId).stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
