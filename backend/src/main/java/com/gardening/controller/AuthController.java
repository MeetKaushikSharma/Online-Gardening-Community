package com.gardening.controller;

import com.gardening.dto.ApiResponse;
import com.gardening.dto.LoginRequest;
import com.gardening.dto.RegisterRequest;
import com.gardening.entity.User;
import com.gardening.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Hardcoded admin credentials
    private static final String ADMIN_EMAIL = "admin";
    private static final String ADMIN_PASSWORD = "admin123@";

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        try {
            if ("admin".equals(request.getUserType())) {
                // Admin login
                if (ADMIN_EMAIL.equals(request.getEmail()) && ADMIN_PASSWORD.equals(request.getPassword())) {
                    User adminUser = new User();
                    adminUser.setId(1);
                    adminUser.setName("Admin User");
                    adminUser.setEmail("admin");
                    adminUser.setRole("admin");

                    ApiResponse<User> response = new ApiResponse<>(true, "Login successful");
                    response.setData(adminUser);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.ok(new ApiResponse<>(false, "Invalid admin credentials"));
                }
            } else if ("gardener".equals(request.getUserType())) {
                // Gardener login
                User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
                if (user != null) {
                    ApiResponse<User> response = new ApiResponse<>(true, "Login successful");
                    response.setData(user);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.ok(new ApiResponse<>(false, "Invalid gardener credentials"));
                }
            }
            return ResponseEntity.ok(new ApiResponse<>(false, "Invalid user type"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequest request) {
        try {
            // Check if email already exists
            if (userRepository.findByEmail(request.getEmail()) != null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Email already registered"));
            }

            // Create new gardener user
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole("gardener");

            userRepository.save(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registration successful! Please login."));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Registration failed: " + e.getMessage()));
        }
    }
}
