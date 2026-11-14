package com.example.hospital.controller;

import com.example.hospital.dto.RegisterRequest;
import com.example.hospital.dto.UserResponse;
import com.example.hospital.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StaffController {

    private final AuthService authService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")

    public List<UserResponse> users() {
        return authService.listDoctors();
    }

    @GetMapping("/doctors")
    @PreAuthorize("isAuthenticated()")

    public List<UserResponse> doctors() {
        return authService.listDoctors();
    }

    @PostMapping({ "/admin/doctors", "/doctors" })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> addDoctor(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createDoctor(req));
    }
}