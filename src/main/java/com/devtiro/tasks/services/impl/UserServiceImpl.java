package com.devtiro.tasks.services.impl;

import com.devtiro.tasks.domain.dto.AuthResponse;
import com.devtiro.tasks.domain.dto.LoginRequest;
import com.devtiro.tasks.domain.dto.SignUpRequest;
import com.devtiro.tasks.domain.entities.User;
import com.devtiro.tasks.repositories.UserRepository;
import com.devtiro.tasks.security.JwtUtils;
import com.devtiro.tasks.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponse registerUser(SignUpRequest signUpRequest) {
        if (existsByUsername(signUpRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (existsByEmail(signUpRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                null,
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                now,
                now
        );

        User savedUser = userRepository.save(user);
        String jwt = jwtUtils.generateJwtToken(savedUser.getUsername());

        return new AuthResponse(jwt, savedUser.getUsername(), savedUser.getEmail());
    }

    @Override
    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String jwt = jwtUtils.generateJwtToken(loginRequest.getUsername());
        User user = findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new AuthResponse(jwt, user.getUsername(), user.getEmail());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}