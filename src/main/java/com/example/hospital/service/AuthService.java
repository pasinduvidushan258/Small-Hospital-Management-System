package com.example.hospital.service;

import com.example.hospital.dto.*;
import com.example.hospital.exception.ApiException;
import com.example.hospital.model.AppUser;
import com.example.hospital.model.Patient;
import com.example.hospital.model.Role;
import com.example.hospital.repository.PatientRepository;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PatientRepository patientRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final AuthenticationManager authManager;


    public UserResponse register(RegisterRequest req) {
        if (userRepo.findByUsername(req.username()).isPresent())
            throw new ApiException(409, "Username already exists");

        AppUser user = AppUser.builder()
                .username(req.username())
                .password(encoder.encode(req.password())) // HASH before saving
                .fullName(req.fullName())
                .role(req.role())
                .build();

        AppUser saved = userRepo.save(user);

        if (saved.getRole() == Role.PATIENT) {
            if (patientRepo.findByFullNameContainingIgnoreCase(saved.getFullName()).isEmpty()) {
                patientRepo.save(Patient.builder()
                        .fullName(saved.getFullName())
                        .email(saved.getUsername().toLowerCase().replace("/", "_") + "@hospital.com")
                        .phone("555-0199")
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .build());
            }
        }

        return new UserResponse(saved.getId(), saved.getUsername(), saved.getFullName(), saved.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));

        AppUser u = userRepo.findByUsername(req.username())
                .orElseThrow(() -> new ApiException(404, "User not found"));

        String token = jwt.generateToken(u.getUsername(), u.getRole().name());
        return new AuthResponse(token, "Bearer", 3_600_000L, u.getUsername(), u.getFullName(), u.getRole().name());
    }

    public UserResponse me(String username) {
        AppUser u = userRepo.findByUsername(username)
                .orElseThrow(() -> new ApiException(404, "User not found"));
        return new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getRole().name());
    }

    public UserResponse createDoctor(RegisterRequest req) {
        if (userRepo.findByUsername(req.username()).isPresent())
            throw new ApiException(409, "Username already exists");

        AppUser user = AppUser.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .fullName(req.fullName())
                .role(Role.DOCTOR)
                .build();

        AppUser saved = userRepo.save(user);
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getFullName(), saved.getRole().name());
    }

    public List<UserResponse> listDoctors() {
        return userRepo.findByRole(Role.DOCTOR).stream()
                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getFullName(), u.getRole().name()))
                .toList();
    }
}