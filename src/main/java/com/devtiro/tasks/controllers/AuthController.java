package com.devtiro.tasks.controllers;

import com.devtiro.tasks.domain.dto.AuthResponse;
import com.devtiro.tasks.domain.dto.LoginRequest;
import com.devtiro.tasks.domain.dto.SignUpRequest;
import com.devtiro.tasks.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = userService.authenticateUser(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

@PostMapping("/signup")
public ResponseEntity<AuthResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
    System.out.println("=== REAL SIGNUP ATTEMPT ===");
    System.out.println("Request: " + signUpRequest.toString());
    
    try {
        AuthResponse authResponse = userService.registerUser(signUpRequest);
        System.out.println("SUCCESS: User registered successfully!");
        return ResponseEntity.ok(authResponse);
    } catch (IllegalArgumentException e) {
        System.out.println("ERROR: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    } catch (Exception e) {
        System.out.println("UNEXPECTED ERROR: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).build();
    }
}

    @PostMapping("/signup-debug")
    public ResponseEntity<String> debugSignup(@RequestBody SignUpRequest request) {
        System.out.println("=== DEBUG SIGNUP ===");
        System.out.println("Received: " + request.toString());
        return ResponseEntity.ok("Parsed successfully: " + request.toString());
    }
}