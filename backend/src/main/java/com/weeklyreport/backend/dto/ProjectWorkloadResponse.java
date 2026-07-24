package com.weeklyreport.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectWorkloadResponse {

    private Long projectId;
    private String projectName;
    private long reportCount;
    private double totalHours;
}