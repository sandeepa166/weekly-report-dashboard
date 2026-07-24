package com.weeklyreport.backend.controller;

import com.weeklyreport.backend.dto.WeeklyReportRequest;
import com.weeklyreport.backend.dto.WeeklyReportResponse;
import com.weeklyreport.backend.service.WeeklyReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class WeeklyReportController {

    private final WeeklyReportService weeklyReportService;

    @PostMapping
    public ResponseEntity<?> createReport(@Valid @RequestBody WeeklyReportRequest request) {
        try {
            WeeklyReportResponse response = weeklyReportService.createReport(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @GetMapping("/my/{userId}")
    public ResponseEntity<List<WeeklyReportResponse>> getMyReports(@PathVariable Long userId) {
        return ResponseEntity.ok(weeklyReportService.getReportsByUser(userId));
    }

    @GetMapping
    public ResponseEntity<List<WeeklyReportResponse>> getAllReports() {
        return ResponseEntity.ok(weeklyReportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Long id) {
        try {
            WeeklyReportResponse response = weeklyReportService.getReportById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody WeeklyReportRequest request
    ) {
        try {
            WeeklyReportResponse response = weeklyReportService.updateReport(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<?> submitReport(@PathVariable Long id) {
        try {
            WeeklyReportResponse response = weeklyReportService.submitReport(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }
}