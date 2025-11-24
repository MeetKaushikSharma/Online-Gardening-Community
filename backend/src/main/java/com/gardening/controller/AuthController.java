package com.gardening.controller;

import com.gardening.dao.UserDao;
import com.gardening.dto.ApiResponse;
import com.gardening.dto.LoginRequest;
import com.gardening.dto.RegisterRequest;
import com.gardening.entity.User;
import com.gardening.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDao userDao;

    public AuthController(UserDao userDao) {
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<User>> login(@RequestBody LoginRequest request) {
        try {
            // Handle admin login (hardcoded for now)
            if ("admin".equals(request.getEmail()) && "admin123@".equals(request.getPassword())) {
                User adminUser = new User();
                adminUser.setId(1);
                adminUser.setUsername("admin");
                adminUser.setEmail("admin");
                adminUser.setRole("ADMIN");
                sanitize(adminUser);
                ApiResponse<User> response = new ApiResponse<>(true, "Login successful");
                response.setData(adminUser);
                return ResponseEntity.ok(response);
            }

            // Handle regular user login
            Optional<User> userOpt = userDao.getUserByEmail(request.getEmail());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid credentials"));
            }

            User user = userOpt.get();
            if (!PasswordUtil.matches(request.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>(false, "Invalid credentials"));
            }

            // Ensure role is uppercase for consistency
            if (user.getRole() != null) {
                user.setRole(user.getRole().toUpperCase());
            }

            sanitize(user);
            ApiResponse<User> response = new ApiResponse<>(true, "Login successful");
            response.setData(user);
            return ResponseEntity.ok(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<User>> register(@RequestBody RegisterRequest request) {
        try {
            if (userDao.getUserByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Email already registered"));
            }

            if (userDao.getUserByUsername(request.getUsername()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(false, "Username already taken"));
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPasswordHash(PasswordUtil.hashPassword(request.getPassword()));
            user.setRole("GARDENER");

            int id = userDao.createUser(user);
            user.setId(id);
            // Ensure role is uppercase
            user.setRole("GARDENER");
            sanitize(user);

            ApiResponse<User> response = new ApiResponse<>(true, "Registration successful");
            response.setData(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(new ApiResponse<>(false, "Registration failed: " + e.getMessage()));
        }
    }

    private void sanitize(User user) {
        user.setPasswordHash(null);
    }
}
