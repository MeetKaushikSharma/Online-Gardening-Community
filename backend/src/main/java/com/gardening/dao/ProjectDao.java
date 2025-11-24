package com.gardening.dao;

import com.gardening.db.DatabaseConnection;
import com.gardening.entity.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectDao {

    public int createProject(Project project) throws SQLException {
        String sql = "INSERT INTO projects (user_id, name, description, progress) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, project.getUserId());
            stmt.setString(2, project.getName());
            stmt.setString(3, project.getDescription());
            stmt.setInt(4, project.getProgress());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Optional<Project> getProjectById(int id) throws SQLException {
        String sql = "SELECT * FROM projects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToProject(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Project> getProjectsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM projects WHERE user_id = ? ORDER BY created_at DESC";
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    projects.add(mapRowToProject(rs));
                }
            }
        }
        return projects;
    }

    public List<Project> getAllProjects() throws SQLException {
        String sql = "SELECT * FROM projects ORDER BY created_at DESC";
        List<Project> projects = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                projects.add(mapRowToProject(rs));
            }
        }
        return projects;
    }

    public boolean updateProject(Project project) throws SQLException {
        String sql = "UPDATE projects SET name = ?, description = ?, progress = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, project.getName());
            stmt.setString(2, project.getDescription());
            stmt.setInt(3, project.getProgress());
            stmt.setInt(4, project.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProject(int id) throws SQLException {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Project mapRowToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getInt("id"));
        project.setUserId(rs.getInt("user_id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        project.setProgress(rs.getInt("progress"));
        project.setCreatedAt(rs.getTimestamp("created_at"));
        return project;
    }
}

