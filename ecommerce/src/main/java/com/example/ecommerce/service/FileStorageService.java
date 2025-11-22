package com.example.ecommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final String uploadDir = "uploads";

    // ✅ File upload logic
    public String storeFile(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty ❌");
        }

        // Folder create karo agar exist nahi karta
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Unique file name generate
        String fileName = System.currentTimeMillis()
                + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);

        // File copy
        Files.copy(file.getInputStream(), filePath,
                StandardCopyOption.REPLACE_EXISTING);

        // URL jo client ko milega
        return "/uploads/" + fileName;
    }

    // ✅ File delete karne ka method (future use)
    public boolean deleteFile(String imageUrl) {

        try {
            if (imageUrl == null || imageUrl.isEmpty()) {
                return false;
            }

            String fileName = imageUrl.replace("/uploads/", "");
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            return Files.deleteIfExists(filePath);

        } catch (Exception e) {
            return false;
        }
    }
}
