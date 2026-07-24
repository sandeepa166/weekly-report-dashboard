package com.weeklyreport.backend.service;

import com.weeklyreport.backend.dto.WeeklyReportRequest;
import com.weeklyreport.backend.dto.WeeklyReportResponse;
import com.weeklyreport.backend.model.Project;
import com.weeklyreport.backend.model.ReportStatus;
import com.weeklyreport.backend.model.User;
import com.weeklyreport.backend.model.WeeklyReport;
import com.weeklyreport.backend.repository.ProjectRepository;
import com.weeklyreport.backend.repository.UserRepository;
import com.weeklyreport.backend.repository.WeeklyReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeeklyReportService {

    private final WeeklyReportRepository weeklyReportRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public WeeklyReportResponse createReport(WeeklyReportRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        WeeklyReport report = WeeklyReport.builder()
                .user(user)
                .project(project)
                .weekStart(request.getWeekStart())
                .weekEnd(request.getWeekEnd())
                .tasksCompleted(request.getTasksCompleted())
                .tasksPlanned(request.getTasksPlanned())
                .blockers(request.getBlockers())
                .hoursWorked(request.getHoursWorked())
                .notes(request.getNotes())
                .status(ReportStatus.DRAFT)
                .build();

        WeeklyReport savedReport = weeklyReportRepository.save(report);

        return mapToResponse(savedReport);
    }

    public List<WeeklyReportResponse> getReportsByUser(Long userId) {
        return weeklyReportRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<WeeklyReportResponse> getAllReports() {
        return weeklyReportRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public WeeklyReportResponse getReportById(Long id) {
        WeeklyReport report = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        return mapToResponse(report);
    }

    public WeeklyReportResponse updateReport(Long id, WeeklyReportRequest request) {

        WeeklyReport report = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        report.setProject(project);
        report.setWeekStart(request.getWeekStart());
        report.setWeekEnd(request.getWeekEnd());
        report.setTasksCompleted(request.getTasksCompleted());
        report.setTasksPlanned(request.getTasksPlanned());
        report.setBlockers(request.getBlockers());
        report.setHoursWorked(request.getHoursWorked());
        report.setNotes(request.getNotes());

        WeeklyReport updatedReport = weeklyReportRepository.save(report);

        return mapToResponse(updatedReport);
    }

    public WeeklyReportResponse submitReport(Long id) {

        WeeklyReport report = weeklyReportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found"));

        report.setStatus(ReportStatus.SUBMITTED);
        report.setSubmittedAt(LocalDateTime.now());

        WeeklyReport submittedReport = weeklyReportRepository.save(report);

        return mapToResponse(submittedReport);
    }

    private WeeklyReportResponse mapToResponse(WeeklyReport report) {
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