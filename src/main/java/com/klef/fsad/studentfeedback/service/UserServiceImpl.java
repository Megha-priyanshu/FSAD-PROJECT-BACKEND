package com.klef.fsad.studentfeedback.service;

import com.klef.fsad.studentfeedback.entity.User;
import com.klef.fsad.studentfeedback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null) {
            // ✅ THIS IS WHAT'S MISSING — save login timestamp
            user.setLastLoginAt(LocalDateTime.now().toString());
            userRepository.save(user);
        }
        return user;
    }

    @Override
    public String register(User user) {
        if (userRepository.existsByEmail(user.getEmail()))
            return "Email already exists";
        user.setCreatedAt(LocalDateTime.now().toString());
        userRepository.save(user);
        return "Account created successfully";
    }

    @Override
    public List<User> getAllByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> getRecentlySignedIn(String role) {
        // ✅ Uses your existing repository method
        return userRepository.findByRoleAndLastLoginAtIsNotNull(role);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}