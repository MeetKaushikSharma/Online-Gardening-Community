package com.gardening.controller;

import com.gardening.dto.ApiResponse;
import com.gardening.entity.Tip;
import com.gardening.repository.TipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tips")
public class TipController {

    @Autowired
    private TipRepository tipRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Tip>>> getAllTips() {
        try {
            List<Tip> tips = tipRepository.findAll();
            ApiResponse<List<Tip>> response = new ApiResponse<>(true, "Tips retrieved successfully");
            response.setData(tips);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Tip>> createTip(@RequestBody Tip tip) {
        try {
            Tip savedTip = tipRepository.save(tip);
            ApiResponse<Tip> response = new ApiResponse<>(true, "Tip created successfully");
            response.setData(savedTip);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Tip>> updateTip(@PathVariable Integer id, @RequestBody Tip tipDetails) {
        try {
            Tip tip = tipRepository.findById(id).orElse(null);
            if (tip == null) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Tip not found"));
            }

            if (tipDetails.getTitle() != null)
                tip.setTitle(tipDetails.getTitle());
            if (tipDetails.getDescription() != null)
                tip.setDescription(tipDetails.getDescription());
            if (tipDetails.getAuthor() != null)
                tip.setAuthor(tipDetails.getAuthor());

            Tip updatedTip = tipRepository.save(tip);
            ApiResponse<Tip> response = new ApiResponse<>(true, "Tip updated successfully");
            response.setData(updatedTip);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteTip(@PathVariable Integer id) {
        try {
            if (!tipRepository.existsById(id)) {
                return ResponseEntity.ok(new ApiResponse<>(false, "Tip not found"));
            }
            tipRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Tip deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse<>(false, "Error: " + e.getMessage()));
        }
    }
}
