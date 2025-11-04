package com.gov.investment.service;

import com.gov.investment.model.Project;
import java.math.BigDecimal;
import java.util.List;

public interface ProjectService {
    
    int createProject(Project project);
    
    Project getProjectById(BigDecimal id);
    
    int updateProject(Project project);
    
    int deleteProject(BigDecimal id);
    
    List<Project> getAllProjects();
}
