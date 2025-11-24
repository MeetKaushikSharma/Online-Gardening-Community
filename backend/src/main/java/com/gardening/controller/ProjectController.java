package com.gardening.controller;

import com.gardening.dto.ApiResponse;
import com.gardening.entity.Project;
import com.gardening.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            ApiResponse<List<Project>> response = new ApiResponse<>(true, "Projects retrieved successfully");
            response.setData(projects);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody Project project) {
        try {
            Project savedProject = projectRepository.save(project);
            ApiResponse<Project> response = new ApiResponse<>(true, "Project created successfully");
            response.setData(savedProject);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable Integer id,
            @RequestBody Project projectDetails) {
        try {
            Project project = projectRepository.findById(id).orElse(null);
            if (project == null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Project not found"));
            }

            if (projectDetails.getName() != null)
                project.setName(projectDetails.getName());
            if (projectDetails.getDescription() != null)
                project.setDescription(projectDetails.getDescription());
            if (projectDetails.getProgress() != null)
                project.setProgress(projectDetails.getProgress());

            Project updatedProject = projectRepository.save(project);
            ApiResponse<Project> response = new ApiResponse<>(true, "Project updated successfully");
            response.setData(updatedProject);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProject(@PathVariable Integer id) {
        try {
            if (!projectRepository.existsById(id)) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Project not found"));
            }
            projectRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Project deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }
}
