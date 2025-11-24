package com.gardening.dao;

import com.gardening.db.DatabaseConnection;
import com.gardening.entity.Comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentDao {

    public int createComment(Comment comment) throws SQLException {
        String sql = "INSERT INTO comments (post_id, user_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, comment.getPostId());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getContent());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public List<Comment> getCommentsByPostId(int postId) throws SQLException {
        String sql = """
                SELECT c.id,
                       c.post_id,
                       c.user_id,
                       c.content,
                       c.created_at,
                       u.username AS author_name
                FROM comments c
                LEFT JOIN users u ON u.id = c.user_id
                WHERE c.post_id = ?
                ORDER BY c.created_at ASC
                """;
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    comments.add(mapRowToComment(rs));
                }
            }
        }
        return comments;
    }

    public boolean deleteComment(int id) throws SQLException {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public Optional<Comment> getCommentById(int commentId) throws SQLException {
        String sql = """
                SELECT c.id,
                       c.post_id,
                       c.user_id,
                       c.content,
                       c.created_at,
                       u.username AS author_name
                FROM comments c
                LEFT JOIN users u ON u.id = c.user_id
                WHERE c.id = ?
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToComment(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Comment mapRowToComment(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getInt("id"));
        comment.setPostId(rs.getInt("post_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setContent(rs.getString("content"));
        comment.setAuthorName(rs.getString("author_name"));
        comment.setCreatedAt(rs.getTimestamp("created_at"));
        return comment;
    }
}

