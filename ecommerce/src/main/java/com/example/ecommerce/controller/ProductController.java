package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.service.FileStorageService;
import com.example.ecommerce.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService,
                             FileStorageService fileStorageService,
                             UserRepository userRepository) {
        this.productService = productService;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

    // ✅ GET ALL PRODUCTS (Public)
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products);
    }

    // ✅ GET PRODUCT BY ID (Public)
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        Product product = productService.getById(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found ❌");
        }

        return ResponseEntity.ok(product);
    }

    // ✅ CREATE PRODUCT (ADMIN ONLY)
    @PostMapping("/add")
    public ResponseEntity<?> createProduct(
            @RequestParam String adminEmail,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam("image") MultipartFile image) {

        try {
            // Step 1: Check admin
            User user = userRepository.findByEmail(adminEmail)
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User not found ❌");
            }

            if (!"ROLE_ADMIN".equals(user.getRole())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied! Only admin can add products 🚫");
            }

            // Step 2: Save image
            String imageUrl = fileStorageService.storeFile(image);

            // Step 3: Create product
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImageUrl(imageUrl);

            Product saved = productService.save(product);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload failed ❌");
        }
    }

    // ✅ UPDATE PRODUCT (ADMIN ONLY)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable String id,
            @RequestParam String adminEmail,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            User user = userRepository.findByEmail(adminEmail).orElse(null);

            if (user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found ❌");

            if (!"ROLE_ADMIN".equals(user.getRole()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Access denied! Only admin can update 🚫");

            Product product = productService.getById(id);

            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product not found ❌");
            }

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);

            if (image != null && !image.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(image);
                product.setImageUrl(imageUrl);
            }

            Product updated = productService.save(product);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Update failed ❌");
        }
    }

    // ✅ DELETE PRODUCT (ADMIN ONLY)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable String id,
            @RequestParam String adminEmail) {

        User user = userRepository.findByEmail(adminEmail).orElse(null);

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found ❌");

        if (!"ROLE_ADMIN".equals(user.getRole()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only admin can delete product 🚫");

        Product product = productService.getById(id);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Product not found ❌");
        }

        productService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted successfully ✅");
    }
}
