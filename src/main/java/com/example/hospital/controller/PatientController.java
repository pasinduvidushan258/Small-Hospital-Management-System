package com.example.hospital.controller;

import com.example.hospital.model.Patient;
import com.example.hospital.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping({ "/api/patients", "/api/admin/patients" })
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")

    public List<Patient> list() {
        return patientService.list();
    }

    @PostMapping({ "/api/patients", "/api/admin/patients" })
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Patient> create(@RequestBody Patient p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(p));
    }

    @DeleteMapping({ "/api/patients/{id}", "/api/admin/patients/{id}" })
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}