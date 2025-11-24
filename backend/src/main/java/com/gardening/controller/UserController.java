package com.gardening.controller;

import com.gardening.dao.UserDao;
import com.gardening.dto.ApiResponse;
import com.gardening.dto.UserRequest;
import com.gardening.entity.User;
import com.gardening.util.PasswordUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userDao.getAllUsers();
            users.forEach(this::sanitize);
            ApiResponse<List<User>> response = new ApiResponse<>(true, "Users retrieved successfully");
            response.setData(users);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load users: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable int id) {
        try {
            Optional<User> userOpt = userDao.getUserById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found"));
            }
            User user = userOpt.get();
            sanitize(user);
            ApiResponse<User> response = new ApiResponse<>(true, "User retrieved successfully");
            response.setData(user);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to load user: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().isBlank()
                    || request.getEmail() == null || request.getEmail().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username and email are required"));
            }

            if (userDao.getUserByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Email already exists"));
            }
            if (userDao.getUserByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username already exists"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setRole(request.getRole() == null ? "GARDENER" : request.getRole().toUpperCase());
            if (request.getPassword() == null || request.getPassword().isBlank()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Password is required"));
            }
            user.setPasswordHash(PasswordUtil.hashPassword(request.getPassword()));

            int newId = userDao.createUser(user);
            user.setId(newId);
            sanitize(user);

            ApiResponse<User> response = new ApiResponse<>(true, "User created successfully");
            response.setData(user);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to create user: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable int id, @RequestBody UserRequest request) {
        try {
            Optional<User> userOpt = userDao.getUserById(id);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found"));
            }

            User user = userOpt.get();
            if (request.getUsername() != null && !request.getUsername().isBlank()
                    && !request.getUsername().equalsIgnoreCase(user.getUsername())) {
                Optional<User> existingUsername = userDao.getUserByUsername(request.getUsername());
                if (existingUsername.isPresent() && existingUsername.get().getId() != id) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(false, "Username already in use"));
                }
                user.setUsername(request.getUsername());
            }
            if (request.getEmail() != null && !request.getEmail().isBlank()
                    && !request.getEmail().equalsIgnoreCase(user.getEmail())) {
                Optional<User> existingEmail = userDao.getUserByEmail(request.getEmail());
                if (existingEmail.isPresent() && existingEmail.get().getId() != id) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse<>(false, "Email already in use"));
                }
                user.setEmail(request.getEmail());
            }
            if (request.getRole() != null && !request.getRole().isBlank()) {
                user.setRole(request.getRole().toUpperCase());
            }
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPasswordHash(PasswordUtil.hashPassword(request.getPassword()));
            }

            userDao.updateUser(user);
            sanitize(user);
            ApiResponse<User> response = new ApiResponse<>(true, "User updated successfully");
            response.setData(user);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to update user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable int id) {
        try {
            if (!userDao.deleteUser(id)) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "User not found"));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully"));
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Failed to delete user: " + e.getMessage()));
        }
    }

    private void sanitize(User user) {
        user.setPasswordHash(null);
    }
}
