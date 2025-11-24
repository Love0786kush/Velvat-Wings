package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

    public Product getById(String id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Product not found"));
    }

    public Product create(Product product) {
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        product.setActive(true);
        return productRepository.save(product);
    }

    public Product update(String id, Product updated) throws Exception {
        Product existing = getById(id);

        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setDiscountPrice(updated.getDiscountPrice());
        existing.setImages(updated.getImages());
        existing.setCategoryId(updated.getCategoryId());
        existing.setTags(updated.getTags());
        existing.setStock(updated.getStock());
        existing.setActive(updated.isActive());
        existing.setUpdatedAt(Instant.now());

        return productRepository.save(existing);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
