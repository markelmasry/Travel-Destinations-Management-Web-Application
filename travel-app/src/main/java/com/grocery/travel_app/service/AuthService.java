package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.InvalidCredentialsException;
import com.grocery.travel_app.mapper.UserMapper;
import com.grocery.travel_app.model.dto.AuthResponse;
import com.grocery.travel_app.model.dto.UserResponse;
import com.grocery.travel_app.model.entity.Role;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.repository.UserRepository;
import com.grocery.travel_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse registerAdmin(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        User admin = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(admin);
        return new AuthResponse("","Admin registered successfully",userMapper.toUserResponse(admin));
    }
    @Transactional
    public AuthResponse registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return new AuthResponse("","User registered successfully",userMapper.toUserResponse(user));
    }
    @Transactional(readOnly = true)
    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        org.springframework.security.core.userdetails.UserDetails springUser =
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getRole().name())
                        .build();
        String token = jwtService.generateToken(springUser);
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
        return AuthResponse.builder()
                .token(token)
                .message("Login successful")
                .user(userResponse)
                .build();
    }
}