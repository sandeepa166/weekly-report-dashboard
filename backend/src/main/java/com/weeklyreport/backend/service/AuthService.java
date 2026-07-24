package com.weeklyreport.backend.service;

import com.weeklyreport.backend.dto.AuthResponse;
import com.weeklyreport.backend.dto.RegisterRequest;
import com.weeklyreport.backend.model.Role;
import com.weeklyreport.backend.model.User;
import com.weeklyreport.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role userRole = Role.TEAM_MEMBER;

        if (request.getRole() != null && !request.getRole().isBlank()) {
            userRole = Role.valueOf(request.getRole().toUpperCase());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                "User registered successfully",
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }
}