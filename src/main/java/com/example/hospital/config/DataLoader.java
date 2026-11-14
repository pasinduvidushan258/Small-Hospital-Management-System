package com.example.hospital.config;

import com.example.hospital.model.AppUser;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Role;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.admin-password:Admin123!}")
    private String adminPassword;

    @Value("${app.seed.doctor-password:Doctor123!}")
    private String doctorPassword;

    @Override
    public void run(String... args) {
        seedUser("admin", "Hospital Administrator", Role.ADMIN, adminPassword);
        seedUser("doctor", "Sample Doctor", Role.DOCTOR, doctorPassword);
        seedPatients();
    }

    private void seedUser(String username, String fullName, Role role, String rawPassword) {
        if (userRepository.findByUsername(username).isEmpty()) {
            userRepository.save(AppUser.builder()
                    .username(username)
                    .password(passwordEncoder.encode(rawPassword))
                    .fullName(fullName)
                    .role(role)
                    .build());
        }
    }

    private void seedPatients() {
        if (patientRepository.count() > 0) {
            return;
        }

        patientRepository.save(Patient.builder()
                .fullName("Alex Morgan")
                .email("alex.morgan@example.com")
                .phone("555-0101")
                .dateOfBirth(LocalDate.of(1990, 5, 14))
                .build());

        patientRepository.save(Patient.builder()
                .fullName("Jordan Lee")
                .email("jordan.lee@example.com")
                .phone("555-0102")
                .dateOfBirth(LocalDate.of(1985, 9, 23))
                .build());
    }
}
