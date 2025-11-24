package com.gardening.controller;

import com.gardening.dao.ProjectDao;
import com.gardening.dao.UserDao;
import com.gardening.dto.ApiResponse;
import com.gardening.dto.ProjectRequest;
import com.gardening.entity.Project;
import com.gardening.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectDao projectDao;
    private final UserDao userDao;

    public ProjectController(ProjectDao projectDao, UserDao userDao) {
        this.projectDao = projectDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Project>>> getAllProjects() {
        try {
            List<Project> projects = projectDao.getAllProjects();
            ApiResponse<List<Project>> response = new ApiResponse<>(true, "Projects retrieved successfully");
            response.setData(projects);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load projects: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Project>>> getProjectsByUser(@PathVariable int userId) {
        try {
            List<Project> projects = projectDao.getProjectsByUserId(userId);
            ApiResponse<List<Project>> response = new ApiResponse<>(true, "Projects retrieved successfully");
            response.setData(projects);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load projects: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Project>> createProject(@RequestBody ProjectRequest request) {
        if (request.getName() == null || request.getName().isBlank()
                || request.getDescription() == null || request.getDescription().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Name and description are required"));
        }

        try {
            Optional<User> userOpt = userDao.getUserById(request.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid user. Please log in again."));
            }

            Project project = new Project();
            project.setUserId(request.getUserId());
            project.setName(request.getName());
            project.setDescription(request.getDescription());
            project.setProgress(request.getProgress() == null ? 0 : request.getProgress());

            int newId = projectDao.createProject(project);
            Project saved = projectDao.getProjectById(newId).orElse(project);

            ApiResponse<Project> response = new ApiResponse<>(true, "Project created successfully");
            response.setData(saved);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to create project: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Project>> updateProject(@PathVariable int id,
                                                              @RequestBody ProjectRequest request) {
        try {
            Optional<Project> projectOpt = projectDao.getProjectById(id);
            if (projectOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Project not found"));
            }

            Project project = projectOpt.get();
            if (request.getName() != null && !request.getName().isBlank()) {
                project.setName(request.getName());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                project.setDescription(request.getDescription());
            }
            if (request.getProgress() != null) {
                project.setProgress(request.getProgress());
            }

            projectDao.updateProject(project);
            Project updated = projectDao.getProjectById(id).orElse(project);
            ApiResponse<Project> response = new ApiResponse<>(true, "Project updated successfully");
            response.setData(updated);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update project: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable int id) {
        try {
            if (!projectDao.deleteProject(id)) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Project not found"));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Project deleted successfully"));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete project: " + e.getMessage()));
        }
    }
}
