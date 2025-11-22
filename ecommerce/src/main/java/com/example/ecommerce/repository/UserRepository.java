package com.example.ecommerce.repository;

import com.example.ecommerce.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    // ✅ Email se user find karne ke liye (Login me use hoga)
    Optional<User> findByEmail(String email);

    // ✅ Register time duplicate email check
    boolean existsByEmail(String email);
}
