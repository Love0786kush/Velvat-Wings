package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.payload.LoginRequest;
import com.example.ecommerce.payload.RegisterRequest;
import com.example.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void testRegisterUser() {

        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        RegisterRequest request = new RegisterRequest(
                "Lovekush",
                "love@test.com",
                "123456"
        );

        when(mockRepo.existsByEmail(request.getEmail())).thenReturn(false);

        userService.registerUser(request);

        Mockito.verify(mockRepo, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void testValidateUser() {

        UserRepository mockRepo = Mockito.mock(UserRepository.class);
        UserService userService = new UserService(mockRepo);

        User user = new User();
        user.setEmail("user@test.com");
        user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("123456"));
        user.setRole("ROLE_USER");

        when(mockRepo.findByEmail("user@test.com"))
                .thenReturn(Optional.of(user));

        LoginRequest login = new LoginRequest("user@test.com", "123456");

        User result = userService.validateUser(login);

        assertNotNull(result);
        assertEquals("ROLE_USER", result.getRole());
    }
}
