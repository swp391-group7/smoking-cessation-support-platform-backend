package com.Swp_391_gr7.smoking_cessation_support_platform_backend.scheduler;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Membership_Package;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.MembershipPackageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class MembershipPackageScheduler {

    private final MembershipPackageRepository repo;

    public MembershipPackageScheduler(MembershipPackageRepository repo) {
        this.repo = repo;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void deactivateExpiredPackages() {
        LocalDateTime today = LocalDateTime.now();
        List<Membership_Package> expired = repo.findAllByIsActiveTrueAndEndDateBefore(today);
        expired.forEach(pkg -> pkg.setActive(false));
        repo.saveAll(expired);
    }
}
