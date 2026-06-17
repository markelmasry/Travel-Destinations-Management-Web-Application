package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.InvalidCredentialsException;
import com.grocery.travel_app.model.dto.AuthRequestDto;
import com.grocery.travel_app.model.dto.AuthResponseDto;
import com.grocery.travel_app.model.entity.Role;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.repository.UserRepository;
import com.grocery.travel_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto registerAdmin(AuthRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Failed admin registration: Username '{}' already exists", request.getUsername());
            throw new DuplicateResourceException("Registration failed. Please try a different username.");
        }

        User admin = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(admin);
        log.info("Successfully registered new admin: {}", admin.getUsername());

        return AuthResponseDto.builder()
                .message("Admin registered successfully")
                .build();
    }

    @Transactional
    public AuthResponseDto registerUser(AuthRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Failed user registration: Username '{}' already exists", request.getUsername());
            throw new DuplicateResourceException("Registration failed. Please try a different username.");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        log.info("Successfully registered new user: {}", user.getUsername());

        return AuthResponseDto.builder()
                .message("User registered successfully")
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponseDto login(AuthRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed: User '{}' not found in database", request.getUsername());
                    return new InvalidCredentialsException("Invalid username or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Login failed: Incorrect password attempt for user '{}'", request.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        org.springframework.security.core.userdetails.UserDetails springUser =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRole().name())
                        .build();

        String token = jwtService.generateToken(springUser);
        log.info("User '{}' successfully logged in", user.getUsername());

        return AuthResponseDto.builder()
                .token(token)
                .message("Login successful")
                .build();
    }
}