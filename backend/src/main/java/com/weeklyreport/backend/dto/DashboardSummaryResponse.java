package com.weeklyreport.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardSummaryResponse {

    private long totalTeamMembers;
    private long submittedReports;
    private long pendingReports;
    private long openBlockers;
    private double complianceRate;
}