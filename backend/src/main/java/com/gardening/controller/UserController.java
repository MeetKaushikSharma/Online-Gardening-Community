package com.gardening.controller;

import com.gardening.dto.ApiResponse;
import com.gardening.entity.User;
import com.gardening.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            ApiResponse<List<User>> response = new ApiResponse<>(true, "Users retrieved successfully");
            response.setData(users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Integer id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                ApiResponse<User> response = new ApiResponse<>(true, "User retrieved successfully");
                response.setData(user);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.ok(new ApiResponse<>(false, "User not found"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Email already exists"));
            }
            User savedUser = userRepository.save(user);
            ApiResponse<User> response = new ApiResponse<>(true, "User created successfully");
            response.setData(savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        try {
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "User not found"));
            }

            if (userDetails.getName() != null)
                user.setName(userDetails.getName());
            if (userDetails.getEmail() != null)
                user.setEmail(userDetails.getEmail());
            if (userDetails.getPassword() != null)
                user.setPassword(userDetails.getPassword());
            if (userDetails.getRole() != null)
                user.setRole(userDetails.getRole());

            User updatedUser = userRepository.save(user);
            ApiResponse<User> response = new ApiResponse<>(true, "User updated successfully");
            response.setData(updatedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Integer id) {
        try {
            if (!userRepository.existsById(id)) {
                return ResponseEntity.ok(new ApiResponse<>(false, "User not found"));
            }
            userRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }
}
