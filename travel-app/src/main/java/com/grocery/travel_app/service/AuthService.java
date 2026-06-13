package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.DuplicateResourceException;
import com.grocery.travel_app.exception.InvalidCredentialsException;
import com.grocery.travel_app.model.entity.Role;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.repository.UserRepository;
import com.grocery.travel_app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void registerAdmin(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        User admin = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_ADMIN)
                .build();

        userRepository.save(admin);
    }
    public void registerUser(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    public String login(String username, String password) {
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
        return jwtService.generateToken(springUser);
    }
}