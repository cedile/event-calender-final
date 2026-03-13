package com.devtiro.tasks.services;

import java.util.Optional;
import java.util.UUID;

import com.devtiro.tasks.domain.dto.AuthResponse;
import com.devtiro.tasks.domain.dto.LoginRequest;
import com.devtiro.tasks.domain.dto.SignUpRequest;
import com.devtiro.tasks.domain.entities.User;

public interface UserService {
    AuthResponse registerUser(SignUpRequest signUpRequest);
    AuthResponse authenticateUser(LoginRequest loginRequest);
    Optional<User> findByUsername(String username);
    Optional<User> findById(UUID id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}