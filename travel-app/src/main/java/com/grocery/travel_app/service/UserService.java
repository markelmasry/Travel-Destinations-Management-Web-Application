package com.grocery.travel_app.service;

import com.grocery.travel_app.exception.ResourceNotFoundException;
import com.grocery.travel_app.mapper.UserMapper;
import com.grocery.travel_app.model.dto.UserResponseDto;
import com.grocery.travel_app.model.entity.User;
import com.grocery.travel_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(Long id) {
        log.info(">>> Service call: Fetching user profile by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to retrieve user: ID {} not found", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });

        log.info("Successfully fetched profile for user ID: {}", id);
        return userMapper.toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        log.info(">>> Service call: Fetching user entity by ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Failed to retrieve user entity: ID {} not found", id);
                    return new ResourceNotFoundException("User not found with id: " + id);
                });
    }

    public User getUserEntityByUsername(String username) {
        log.info(">>> Service call: Fetching user entity by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Failed to retrieve user entity: Username '{}' not found", username);
                    return new ResourceNotFoundException(
                            "Database consistency error: User not found with username: " + username
                    );
                });
    }
}