package com.weeklyreport.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubmissionStatusResponse {

    private Long userId;
    private String userName;
    private String email;
    private String status;
}