package com.weeklyreport.backend.repository;

import com.weeklyreport.backend.model.ReportStatus;
import com.weeklyreport.backend.model.WeeklyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WeeklyReportRepository extends JpaRepository<WeeklyReport, Long> {

    List<WeeklyReport> findByUserId(Long userId);

    List<WeeklyReport> findByProjectId(Long projectId);

    List<WeeklyReport> findByStatus(ReportStatus status);

    List<WeeklyReport> findByWeekStartGreaterThanEqualAndWeekEndLessThanEqual(
            LocalDate startDate,
            LocalDate endDate
    );
}