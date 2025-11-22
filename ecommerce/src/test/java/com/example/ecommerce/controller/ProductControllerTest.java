package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Add Product
    @Test
    void testAddProduct() throws Exception {

        Product product = new Product(
                "Laptop",
                "Powerfull laptop",
                55000,
                "img.png"
        );

        mockMvc.perform(post("/api/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    // ✅ Get All Products
    @Test
    void testGetAllProducts() throws Exception {

        mockMvc.perform(get("/api/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // ✅ Delete Product
    @Test
    void testDeleteProduct() throws Exception {

        String productId = "PUT_VALID_PRODUCT_ID";

        mockMvc.perform(delete("/api/products/delete/" + productId))
                .andExpect(status().isOk());
    }
}
