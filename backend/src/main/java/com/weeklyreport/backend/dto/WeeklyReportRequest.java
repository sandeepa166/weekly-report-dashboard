package com.weeklyreport.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WeeklyReportRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long projectId;

    @NotNull
    private LocalDate weekStart;

    @NotNull
    private LocalDate weekEnd;

    private String tasksCompleted;

    private String tasksPlanned;

    private String blockers;

    private Double hoursWorked;

    private String notes;
}