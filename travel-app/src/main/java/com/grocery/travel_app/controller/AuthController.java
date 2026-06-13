package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.AuthRequest;
import com.grocery.travel_app.model.dto.AuthResponse;
import com.grocery.travel_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) {
        authService.registerUser(request.getUsername(), request.getPassword()); // Assumes this exists in AuthService
        AuthResponse response = AuthResponse.builder()
                .message("User registered successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AuthRequest request) {
        authService.registerAdmin(request.getUsername(), request.getPassword());
        AuthResponse response = AuthResponse.builder()
                .message("Admin registered successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        AuthResponse response = AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .build();
        return ResponseEntity.ok(response);
    }
}