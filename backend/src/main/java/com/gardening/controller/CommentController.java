package com.gardening.controller;

import com.gardening.dao.CommentDao;
import com.gardening.dao.PostDao;
import com.gardening.dao.UserDao;
import com.gardening.dto.ApiResponse;
import com.gardening.dto.CommentRequest;
import com.gardening.entity.Comment;
import com.gardening.entity.Post;
import com.gardening.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentDao commentDao;
    private final PostDao postDao;
    private final UserDao userDao;

    public CommentController(CommentDao commentDao, PostDao postDao, UserDao userDao) {
        this.commentDao = commentDao;
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Comment>>> getComments(@PathVariable int postId) {
        try {
            List<Comment> comments = commentDao.getCommentsByPostId(postId);
            ApiResponse<List<Comment>> response = new ApiResponse<>(true, "Comments retrieved successfully");
            response.setData(comments);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load comments: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Comment>> addComment(@PathVariable int postId,
                                                           @RequestBody CommentRequest request) {
        if (request.getContent() == null || request.getContent().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Comment content is required"));
        }

        try {
            Optional<Post> postOpt = postDao.getPostById(postId);
            if (postOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Post not found"));
            }
            Optional<User> userOpt = userDao.getUserById(request.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid user. Please log in again."));
            }

            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setUserId(request.getUserId());
            comment.setContent(request.getContent());

            int newId = commentDao.createComment(comment);
            Comment saved = commentDao.getCommentById(newId).orElse(comment);
            if (saved.getAuthorName() == null) {
                saved.setAuthorName(userOpt.get().getUsername());
            }

            ApiResponse<Comment> response = new ApiResponse<>(true, "Comment added successfully");
            response.setData(saved);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to add comment: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable int postId, @PathVariable int commentId) {
        try {
            Optional<Post> postOpt = postDao.getPostById(postId);
            if (postOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Post not found"));
            }
            if (!commentDao.deleteComment(commentId)) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Comment not found"));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Comment deleted successfully"));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete comment: " + e.getMessage()));
        }
    }
}

