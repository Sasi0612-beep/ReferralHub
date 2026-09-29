package com.referralhub.service;

import com.referralhub.dto.LoginRequest;
import com.referralhub.dto.RegisterRequest;
import com.referralhub.dto.UserResponse;
import com.referralhub.model.User;
import com.referralhub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users;

    public AuthService(UserRepository users) { this.users = users; }

    public UserResponse login(LoginRequest request) {
        User user = users.findByEmail(request.email().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!user.getPassword().equals(request.password())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return toResponse(user);
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (request.name() == null || request.name().isBlank()
                || request.email() == null || request.email().isBlank()
                || request.password() == null || request.password().length() < 6
                || request.role() == null) {
            throw new IllegalArgumentException("Name, email, role and a password of at least 6 characters are required");
        }

        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("An account with this email already exists");
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(email)
                .password(request.password())
                .role(request.role())
                .build();
        return toResponse(users.save(user));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}
