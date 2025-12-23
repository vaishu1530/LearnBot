package com.example.learnbot.service;

import com.example.learnbot.model.User;
import com.example.learnbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // LOGIN METHOD - Required for MainController
    public User login(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // IMPORTANT: Use passwordEncoder.matches for BCrypt passwords
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null; // Login failed
    }

    // REGISTRATION METHOD
    public void save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Encrypt password before saving to DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}