package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        try {
            User user = userService.getByEmail(email);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User not found ❌");
        }
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<?> updateProfile(@PathVariable String email,
                                           @RequestBody User updated) {
        try {
            User saved = userService.updateProfile(email, updated);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Update failed ❌ " + e.getMessage());
        }
    }
}
