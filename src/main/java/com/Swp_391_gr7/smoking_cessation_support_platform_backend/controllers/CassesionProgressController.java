//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;
//
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.Cessation_Progress;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.DailyLogRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/logs")
//@RequiredArgsConstructor
//public class CassesionProgressController {
//
//    private final DailyLogRepository logRepository;
//
//    @PostMapping("/create-log")
//    public ResponseEntity<Cessation_Progress> createLog(@RequestBody Cessation_Progress log) {
//        log.setId(null);
//        log.setLogDate(LocalDate.now());
//        Cessation_Progress savedLog = logRepository.save(log);
//        return ResponseEntity.ok(savedLog);
//    }
//
//    @GetMapping("display-all-logs")
//    public ResponseEntity<List<Cessation_Progress>> getAllLogs() {
//        return ResponseEntity.ok(logRepository.findAll());
//    }
//
//    @GetMapping("/user/{userId}/display-logs-by-user")
//    public ResponseEntity<List<Cessation_Progress>> getLogsByUser(@PathVariable UUID userId) {
//        return ResponseEntity.ok(logRepository.findByUserId(userId));
//    }
//
//    @GetMapping("/user/{userId}/date/{date}/display-log-by-user-and-date")
//    public ResponseEntity<Optional<Cessation_Progress>> getLogByUserAndDate(@PathVariable UUID userId,@PathVariable String date) {
//        LocalDate logDate = LocalDate.parse(date);
//        return ResponseEntity.ok(logRepository.findByUserIdAndLogDate(userId, logDate));
//    }
//
//    @DeleteMapping("/{id}/delete-log-by-id")
//    public ResponseEntity<Void> deleteLog(@PathVariable UUID id) {
//        logRepository.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
