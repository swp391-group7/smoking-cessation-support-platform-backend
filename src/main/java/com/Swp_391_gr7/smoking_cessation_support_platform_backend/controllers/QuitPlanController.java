// src/main/java/com/Swp_391_gr7/smoking_cessation_support_platform_backend/controllers/QuitPlanController.java
package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;

import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanRequest;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.plan.QuitPlanResponse;
import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.QuitPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/quit-plans")
@RequiredArgsConstructor
public class QuitPlanController {
    private final QuitPlanService quitPlanService;

    @PostMapping("/create plan")
    public ResponseEntity<QuitPlanResponse> create(@RequestBody QuitPlanRequest request) {
        return ResponseEntity.ok(quitPlanService.create(request));
    }

    @PutMapping("/{id}/update plan")
    public ResponseEntity<QuitPlanResponse> update(@PathVariable UUID id, @RequestBody QuitPlanRequest request) {
        return ResponseEntity.ok(quitPlanService.update(id, request));
    }

    @GetMapping("/{id}/display plan")
    public ResponseEntity<QuitPlanResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(quitPlanService.get(id));
    }

    @DeleteMapping("/{id}/delete plan")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        quitPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}