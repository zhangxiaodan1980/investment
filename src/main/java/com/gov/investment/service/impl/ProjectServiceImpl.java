package com.gov.investment.service.impl;

import com.gov.investment.mapper.ProjectMapper;
import com.gov.investment.model.Project;
import com.gov.investment.service.ProjectService;
import com.gov.investment.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectMapper projectMapper;
    
    @Autowired
    private SnowflakeIdWorker snowflakeIdWorker;
    
    @Override
    public int createProject(Project project) {
        project.setId(snowflakeIdWorker.nextBigDecimalId());
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());
        return projectMapper.insertProject(project);
    }
    
    @Override
    public Project getProjectById(BigDecimal id) {
        return projectMapper.selectProjectById(id);
    }
    
    @Override
    public int updateProject(Project project) {
        project.setUpdateTime(new Date());
        return projectMapper.updateProject(project);
    }
    
    @Override
    public int deleteProject(BigDecimal id) {
        return projectMapper.deleteProjectById(id);
    }
    
    @Override
    public List<Project> getAllProjects() {
        return projectMapper.selectAllProjects();
    }
}
