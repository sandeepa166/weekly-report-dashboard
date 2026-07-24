package com.weeklyreport.backend.dto;

import com.weeklyreport.backend.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class WeeklyReportResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long projectId;
    private String projectName;

    private LocalDate weekStart;
    private LocalDate weekEnd;

    private String tasksCompleted;
    private String tasksPlanned;
    private String blockers;
    private Double hoursWorked;
    private String notes;

    private ReportStatus status;
    private LocalDateTime submittedAt;
}