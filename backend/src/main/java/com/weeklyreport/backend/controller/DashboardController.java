package com.weeklyreport.backend.controller;

import com.weeklyreport.backend.dto.DashboardSummaryResponse;
import com.weeklyreport.backend.dto.ProjectWorkloadResponse;
import com.weeklyreport.backend.dto.SubmissionStatusResponse;
import com.weeklyreport.backend.dto.WeeklyReportResponse;
import com.weeklyreport.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekStart,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekEnd
    ) {
        return ResponseEntity.ok(dashboardService.getSummary(weekStart, weekEnd));
    }

    @GetMapping("/reports")
    public ResponseEntity<List<WeeklyReportResponse>> getFilteredReports(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekStart,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekEnd,

            @RequestParam(required = false) Long userId,

            @RequestParam(required = false) Long projectId
    ) {
        return ResponseEntity.ok(
                dashboardService.getFilteredReports(weekStart, weekEnd, userId, projectId)
        );
    }

    @GetMapping("/submission-status")
    public ResponseEntity<List<SubmissionStatusResponse>> getSubmissionStatus(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekStart,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekEnd
    ) {
        return ResponseEntity.ok(
                dashboardService.getSubmissionStatus(weekStart, weekEnd)
        );
    }

    @GetMapping("/workload-by-project")
    public ResponseEntity<List<ProjectWorkloadResponse>> getWorkloadByProject(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekStart,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate weekEnd
    ) {
        return ResponseEntity.ok(
                dashboardService.getWorkloadByProject(weekStart, weekEnd)
        );
    }

    @GetMapping("/recent-reports")
    public ResponseEntity<List<WeeklyReportResponse>> getRecentReports() {
        return ResponseEntity.ok(dashboardService.getRecentReports());
    }
}