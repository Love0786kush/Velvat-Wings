package com.example.ecommerce.controller;

import com.example.ecommerce.payload.LoginRequest;
import com.example.ecommerce.payload.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Register Success
    @Test
    void testRegisterSuccess() throws Exception {

        String email = "user_" + UUID.randomUUID() + "@gmail.com";

        RegisterRequest request = new RegisterRequest(
                "Test User",
                email,
                "123456"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User Registered Successfully ✅"));
    }

    // ✅ Duplicate Email Test
    @Test
    void testRegisterDuplicateEmail() throws Exception {

        String email = "duplicate@gmail.com";

        RegisterRequest request = new RegisterRequest(
                "Duplicate",
                email,
                "123456"
        );

        // First request
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Second request should fail
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ✅ Login Success
    @Test
    void testLoginSuccess() throws Exception {

        LoginRequest login = new LoginRequest(
                "duplicate@gmail.com",
                "123456"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk());
    }

    // ❌ Login Fail
    @Test
    void testLoginFail() throws Exception {

        LoginRequest login = new LoginRequest(
                "wrong@gmail.com",
                "wrongpass"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
