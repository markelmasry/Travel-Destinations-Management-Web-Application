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
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody AuthRequest request) {
        AuthResponse response = authService.registerUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@RequestBody AuthRequest request) {
        AuthResponse response = authService.registerAdmin(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}