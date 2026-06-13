package com.grocery.travel_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.grocery.travel_app.repository.UserRepository;
import com.grocery.travel_app.model.entity.User;
import static com.grocery.travel_app.model.entity.Role.ROLE_ADMIN;
import static com.grocery.travel_app.model.entity.Role.ROLE_USER;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Method 1: Executing CommandLineRunner Data Seeder...");
        seedData();
    }

    private void seedData() {
        if (userRepository.count() == 0) {

            if (!userRepository.existsByUsername("mark")) {
                User regularUser = new User();
                regularUser.setUsername("mark");
                regularUser.setPassword(passwordEncoder.encode("1234"));
                regularUser.setRole(ROLE_USER);
                userRepository.save(regularUser);
            }

            if (!userRepository.existsByUsername("admin")) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin1234"));
                adminUser.setRole(ROLE_ADMIN);
                userRepository.save(adminUser);
            }
        }
    }
}