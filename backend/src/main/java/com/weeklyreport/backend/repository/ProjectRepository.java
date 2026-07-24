package com.weeklyreport.backend.repository;

import com.weeklyreport.backend.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
