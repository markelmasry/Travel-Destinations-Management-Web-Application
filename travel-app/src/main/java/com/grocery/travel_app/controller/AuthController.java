package com.grocery.travel_app.controller;

import com.grocery.travel_app.model.dto.AuthRequestDto;
import com.grocery.travel_app.model.dto.AuthResponseDto;
import com.grocery.travel_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register-user")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody AuthRequestDto request) {
        log.info(">>> API call: Register regular user: {}", request.getUsername());
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponseDto> registerAdmin(@Valid @RequestBody AuthRequestDto request) {
        log.info(">>> API call: Register admin user: {}", request.getUsername());
        return ResponseEntity.ok(authService.registerAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto request) {
        log.info(">>> API call: Login attempt for user: {}", request.getUsername());
        return ResponseEntity.ok(authService.login(request));
    }
}