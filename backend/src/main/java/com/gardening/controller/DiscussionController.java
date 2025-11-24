package com.gardening.controller;

import com.gardening.dto.ApiResponse;
import com.gardening.entity.Discussion;
import com.gardening.repository.DiscussionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/discussions")
public class DiscussionController {

    @Autowired
    private DiscussionRepository discussionRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Discussion>>> getAllDiscussions() {
        try {
            List<Discussion> discussions = discussionRepository.findAll();
            ApiResponse<List<Discussion>> response = new ApiResponse<>(true, "Discussions retrieved successfully");
            response.setData(discussions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Discussion>> createDiscussion(@RequestBody Discussion discussion) {
        try {
            Discussion savedDiscussion = discussionRepository.save(discussion);
            ApiResponse<Discussion> response = new ApiResponse<>(true, "Discussion created successfully");
            response.setData(savedDiscussion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Discussion>> updateDiscussion(@PathVariable Integer id,
            @RequestBody Discussion discussionDetails) {
        try {
            Discussion discussion = discussionRepository.findById(id).orElse(null);
            if (discussion == null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Discussion not found"));
            }

            if (discussionDetails.getTopic() != null)
                discussion.setTopic(discussionDetails.getTopic());
            if (discussionDetails.getContent() != null)
                discussion.setContent(discussionDetails.getContent());
            if (discussionDetails.getAuthor() != null)
                discussion.setAuthor(discussionDetails.getAuthor());
            if (discussionDetails.getComments() != null)
                discussion.setComments(discussionDetails.getComments());

            Discussion updatedDiscussion = discussionRepository.save(discussion);
            ApiResponse<Discussion> response = new ApiResponse<>(true, "Discussion updated successfully");
            response.setData(updatedDiscussion);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteDiscussion(@PathVariable Integer id) {
        try {
            if (!discussionRepository.existsById(id)) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Discussion not found"));
            }
            discussionRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Discussion deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }
}
