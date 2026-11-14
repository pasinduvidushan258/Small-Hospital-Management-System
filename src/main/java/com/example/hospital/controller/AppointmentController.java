package com.example.hospital.controller;

import com.example.hospital.dto.*;
import com.example.hospital.model.AppUser;
import com.example.hospital.repository.UserRepository;
import com.example.hospital.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService apptService;
    private final UserRepository userRepo;

    @PostMapping({"/patients/appointments", "/appointments"})
    @PreAuthorize("hasRole('PATIENT')")

    public ResponseEntity<AppointmentResponse> book(Authentication auth,
            @Valid @RequestBody AppointmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apptService.book(auth.getName(), req));
    }

    @GetMapping({ "/doctors/appointments", "/appointments" })
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public List<AppointmentResponse> forDoctor(Authentication auth) {
        AppUser doctor = userRepo.findByUsername(auth.getName()).orElseThrow();
        return apptService.listForDoctor(doctor);
    }

    @PatchMapping("/appointments/{id}/status")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public AppointmentResponse updateStatus(@PathVariable Long id,
            @Valid @RequestBody StatusRequest req,
            Authentication auth) {
        return apptService.updateStatus(id, req.status(), auth.getName());
    }
}