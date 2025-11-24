package com.example.ecommerce.service;

import com.example.ecommerce.model.User;
import com.example.ecommerce.payload.LoginRequest;
import com.example.ecommerce.payload.RegisterRequest;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register normal user
    public void registerUser(RegisterRequest request) throws Exception {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: bcrypt
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    // Register admin
    public void registerAdmin(RegisterRequest request) throws Exception {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new Exception("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
    }

    public User validateUser(LoginRequest request) throws Exception {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new Exception("User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new Exception("Invalid password");
        }

        return user;
    }

    public User getByEmail(String email) throws Exception {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));
    }

    public User updateProfile(String email, User updated) throws Exception {

        User user = getByEmail(email);

        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setPhone(updated.getPhone());
        user.setBirthdate(updated.getBirthdate());
        user.setProfileImage(updated.getProfileImage());

        if (updated.getUsername() != null) {
            user.setUsername(updated.getUsername());
        }

        return userRepository.save(user);
    }
}
