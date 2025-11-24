package com.gardening.dao;

import com.gardening.db.DatabaseConnection;
import com.gardening.entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostDao {

    public int createPost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (user_id, title, content, category) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, post.getUserId());
            stmt.setString(2, post.getTitle());
            stmt.setString(3, post.getContent());
            stmt.setString(4, post.getCategory());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Optional<Post> getPostById(int id) throws SQLException {
        String sql = baseSelect() + " WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPost(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Post> getPostsByUserId(int userId) throws SQLException {
        String sql = baseSelect() + " WHERE p.user_id = ? ORDER BY p.created_at DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapRowToPost(rs));
                }
            }
        }
        return posts;
    }

    public List<Post> getPostsByCategory(String category) throws SQLException {
        String sql = baseSelect() + " WHERE p.category = ? ORDER BY p.created_at DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapRowToPost(rs));
                }
            }
        }
        return posts;
    }

    public List<Post> getAllPosts() throws SQLException {
        String sql = baseSelect() + " ORDER BY p.created_at DESC";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                posts.add(mapRowToPost(rs));
            }
        }
        return posts;
    }

    public boolean updatePost(Post post) throws SQLException {
        String sql = "UPDATE posts SET title = ?, content = ?, category = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setString(3, post.getCategory());
            stmt.setInt(4, post.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePost(int id) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private String baseSelect() {
        return """
                SELECT p.id,
                       p.user_id,
                       p.title,
                       p.content,
                       p.category,
                       p.created_at,
                       u.username AS author_name
                FROM posts p
                LEFT JOIN users u ON u.id = p.user_id
                """;
    }

    private Post mapRowToPost(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("id"));
        post.setUserId(rs.getInt("user_id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setCategory(rs.getString("category"));
        post.setAuthorName(rs.getString("author_name"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        return post;
    }
}

