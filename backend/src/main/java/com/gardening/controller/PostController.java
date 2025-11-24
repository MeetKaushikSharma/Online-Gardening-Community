package com.gardening.controller;

import com.gardening.dao.PostDao;
import com.gardening.dao.UserDao;
import com.gardening.dto.ApiResponse;
import com.gardening.dto.PostRequest;
import com.gardening.entity.Post;
import com.gardening.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostDao postDao;
    private final UserDao userDao;

    public PostController(PostDao postDao, UserDao userDao) {
        this.postDao = postDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Post>>> getPosts(@RequestParam(value = "category", required = false) String category) {
        try {
            List<Post> posts = category == null || category.isBlank()
                    ? postDao.getAllPosts()
                    : postDao.getPostsByCategory(category.toUpperCase());
            ApiResponse<List<Post>> response = new ApiResponse<>(true, "Posts retrieved successfully");
            response.setData(posts);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load posts: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getPost(@PathVariable int id) {
        try {
            Optional<Post> postOpt = postDao.getPostById(id);
            if (postOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Post not found"));
            }
            ApiResponse<Post> response = new ApiResponse<>(true, "Post retrieved successfully");
            response.setData(postOpt.get());
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load post: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody PostRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getContent() == null || request.getContent().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Title and content are required"));
        }

        try {
            Optional<User> userOpt = userDao.getUserById(request.getUserId());
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Invalid user. Please log in again."));
            }

            Post post = new Post();
            post.setUserId(request.getUserId());
            post.setTitle(request.getTitle());
            post.setContent(request.getContent());
            post.setCategory(normalizeCategory(request.getCategory()));

            int newId = postDao.createPost(post);
            Post savedPost = postDao.getPostById(newId).orElse(post);
            if (savedPost.getAuthorName() == null) {
                savedPost.setAuthorName(userOpt.get().getUsername());
            }

            ApiResponse<Post> response = new ApiResponse<>(true, "Post created successfully");
            response.setData(savedPost);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to create post: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> updatePost(@PathVariable int id, @RequestBody PostRequest request) {
        try {
            Optional<Post> postOpt = postDao.getPostById(id);
            if (postOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Post not found"));
            }

            Post post = postOpt.get();
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                post.setTitle(request.getTitle());
            }
            if (request.getContent() != null && !request.getContent().isBlank()) {
                post.setContent(request.getContent());
            }
            if (request.getCategory() != null && !request.getCategory().isBlank()) {
                post.setCategory(normalizeCategory(request.getCategory()));
            }

            postDao.updatePost(post);
            Post updated = postDao.getPostById(id).orElse(post);
            ApiResponse<Post> response = new ApiResponse<>(true, "Post updated successfully");
            response.setData(updated);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update post: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable int id) {
        try {
            if (!postDao.deletePost(id)) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "Post not found"));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Post deleted successfully"));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete post: " + e.getMessage()));
        }
    }

    private String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "DISCUSSION";
        }
        String normalized = category.trim().toUpperCase();
        return ("TIP".equals(normalized) || "DISCUSSION".equals(normalized)) ? normalized : "DISCUSSION";
    }
}

