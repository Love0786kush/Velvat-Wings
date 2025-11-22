package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ SAVE PRODUCT
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // ✅ GET ALL PRODUCTS
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // ✅ GET PRODUCT BY ID
    public Product getById(String id) {
        Optional<Product> optional = productRepository.findById(id);
        return optional.orElse(null);
    }

    // ✅ UPDATE PRODUCT
    public Product update(String id, Product updatedProduct) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found ❌"));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setImageUrl(updatedProduct.getImageUrl());

        return productRepository.save(existing);
    }

    // ✅ DELETE PRODUCT
    public void delete(String id) {

        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found ❌");
        }

        productRepository.deleteById(id);
    }

    // ✅ SEARCH PRODUCT (optional feature)
    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
