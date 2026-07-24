package com.weeklyreport.backend.service;

import com.weeklyreport.backend.dto.DashboardSummaryResponse;
import com.weeklyreport.backend.dto.ProjectWorkloadResponse;
import com.weeklyreport.backend.dto.SubmissionStatusResponse;
import com.weeklyreport.backend.dto.WeeklyReportResponse;
import com.weeklyreport.backend.model.ReportStatus;
import com.weeklyreport.backend.model.Role;
import com.weeklyreport.backend.model.User;
import com.weeklyreport.backend.model.WeeklyReport;
import com.weeklyreport.backend.repository.UserRepository;
import com.weeklyreport.backend.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final WeeklyReportRepository weeklyReportRepository;
    private final UserRepository userRepository;

    public DashboardSummaryResponse getSummary(LocalDate weekStart, LocalDate weekEnd) {

        List<WeeklyReport> reports = getReportsForDateRange(weekStart, weekEnd);

        long totalTeamMembers = userRepository.countByRole(Role.TEAM_MEMBER);

        long submittedReports = reports.stream()
                .filter(report -> report.getStatus() == ReportStatus.SUBMITTED)
                .count();

        long submittedMembers = reports.stream()
                .filter(report -> report.getStatus() == ReportStatus.SUBMITTED)
                .map(report -> report.getUser().getId())
                .distinct()
                .count();

        long pendingReports = Math.max(totalTeamMembers - submittedMembers, 0);

        long openBlockers = reports.stream()
                .filter(report -> report.getBlockers() != null && !report.getBlockers().isBlank())
                .count();

        double complianceRate = 0;

        if (totalTeamMembers > 0) {
            complianceRate = (submittedMembers * 100.0) / totalTeamMembers;
        }

        return new DashboardSummaryResponse(
                totalTeamMembers,
                submittedReports,
                pendingReports,
                openBlockers,
                Math.round(complianceRate * 100.0) / 100.0
        );
    }

    public List<WeeklyReportResponse> getFilteredReports(
            LocalDate weekStart,
            LocalDate weekEnd,
            Long userId,
            Long projectId
    ) {
        return getReportsForDateRange(weekStart, weekEnd)
                .stream()
                .filter(report -> userId == null || report.getUser().getId().equals(userId))
                .filter(report -> projectId == null || report.getProject().getId().equals(projectId))
                .map(this::mapToWeeklyReportResponse)
                .toList();
    }

    public List<SubmissionStatusResponse> getSubmissionStatus(LocalDate weekStart, LocalDate weekEnd) {

        List<WeeklyReport> reports = getReportsForDateRange(weekStart, weekEnd);

        List<User> teamMembers = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.TEAM_MEMBER)
                .toList();

        return teamMembers.stream()
                .map(user -> {
                    boolean hasSubmitted = reports.stream()
                            .anyMatch(report ->
                                    report.getUser().getId().equals(user.getId())
                                            && report.getStatus() == ReportStatus.SUBMITTED
                            );

                    String status = hasSubmitted ? "SUBMITTED" : "PENDING";

                    return new SubmissionStatusResponse(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            status
                    );
                })
                .toList();
    }

    public List<ProjectWorkloadResponse> getWorkloadByProject(LocalDate weekStart, LocalDate weekEnd) {

        List<WeeklyReport> reports = getReportsForDateRange(weekStart, weekEnd);

        Map<Long, List<WeeklyReport>> reportsByProject = reports.stream()
                .collect(Collectors.groupingBy(report -> report.getProject().getId()));

        return reportsByProject.values()
                .stream()
                .map(projectReports -> {
                    WeeklyReport firstReport = projectReports.get(0);

                    double totalHours = projectReports.stream()
                            .mapToDouble(report -> report.getHoursWorked() == null ? 0 : report.getHoursWorked())
                            .sum();

                    return new ProjectWorkloadResponse(
                            firstReport.getProject().getId(),
                            firstReport.getProject().getName(),
                            projectReports.size(),
                            totalHours
                    );
                })
                .toList();
    }

    public List<WeeklyReportResponse> getRecentReports() {
        return weeklyReportRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(
                        WeeklyReport::getSubmittedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .limit(5)
                .map(this::mapToWeeklyReportResponse)
                .toList();
    }

    private List<WeeklyReport> getReportsForDateRange(LocalDate weekStart, LocalDate weekEnd) {
        return weeklyReportRepository.findAll()
                .stream()
                .filter(report -> {
                    if (weekStart == null || weekEnd == null) {
                        return true;
                    }

                    return !report.getWeekStart().isBefore(weekStart)
                            && !report.getWeekEnd().isAfter(weekEnd);
                })
                .toList();
    }

    private WeeklyReportResponse mapToWeeklyReportResponse(WeeklyReport report) {
        return new WeeklyReportResponse(
                report.getId(),
                report.getUser().getId(),
                report.getUser().getName(),
                report.getProject().getId(),
                report.getProject().getName(),
                report.getWeekStart(),
                report.getWeekEnd(),
                report.getTasksCompleted(),
                report.getTasksPlanned(),
                report.getBlockers(),
                report.getHoursWorked(),
                report.getNotes(),
                report.getStatus(),
                report.getSubmittedAt()
        );
    }
}