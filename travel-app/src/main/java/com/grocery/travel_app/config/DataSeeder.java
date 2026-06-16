package com.grocery.travel_app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.grocery.travel_app.repository.UserRepository;
import com.grocery.travel_app.model.entity.User;
import java.util.List;
import static com.grocery.travel_app.model.entity.Role.ROLE_ADMIN;
import static com.grocery.travel_app.model.entity.Role.ROLE_USER;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }

    private void seedData() {
        if (userRepository.count() == 0) {

                User regularUser = new User();
                regularUser.setUsername("mark");
                regularUser.setPassword(passwordEncoder.encode("1234"));
                regularUser.setRole(ROLE_USER);

                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin1234"));
                adminUser.setRole(ROLE_ADMIN);

                userRepository.saveAll(List.of(adminUser, regularUser));

                System.out.println("------------------------------------------------");
                System.out.println(">>> SEEDER: Admin and User accounts created!");
                System.out.println("------------------------------------------------");

        }
    }
}