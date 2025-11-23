package com.example.ecommerce.controller;

import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class ProfileController {

    private static final String UPLOAD_DIR = "uploads/profile/";

    @Autowired
    private UserRepository userRepository;

    // ================= GET USER PROFILE =================
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Principal principal) {

        String email = principal.getName();  // JWT se user email nikalna
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }

    // ================= UPDATE PROFILE =================
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, Principal principal) {

        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());
        user.setBirthdate(updatedUser.getBirthdate());

        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // ================= UPLOAD PROFILE IMAGE =================
    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("image") MultipartFile file,
            Principal principal
    ) {
        try {
            String email = principal.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Create directory if not exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) dir.mkdirs();

            // Generate file name
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, fileName);

            // Save file
            Files.write(filePath, file.getBytes());

            // Save image path in DB
            user.setProfileImage("/" + UPLOAD_DIR + fileName);
            userRepository.save(user);

            return ResponseEntity.ok(user.getProfileImage());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading image ❌");
        }
    }
}
