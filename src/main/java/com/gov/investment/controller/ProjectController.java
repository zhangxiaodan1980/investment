package com.gov.investment.controller;

import com.gov.investment.model.Project;
import com.gov.investment.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        try {
            int result = projectService.createProject(project);
            if (result > 0) {
                return new ResponseEntity<>("项目创建成功", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("项目创建失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable BigDecimal id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return new ResponseEntity<>(project, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("项目不存在", HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable BigDecimal id, @RequestBody Project project) {
        try {
            project.setId(id);
            int result = projectService.updateProject(project);
            if (result > 0) {
                return new ResponseEntity<>("项目更新成功", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("项目更新失败", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable BigDecimal id) {
        int result = projectService.deleteProject(id);
        if (result > 0) {
            return new ResponseEntity<>("项目删除成功", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("项目删除失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
}
