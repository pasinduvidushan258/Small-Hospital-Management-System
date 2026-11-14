package com.example.hospital.controller;

import com.example.hospital.dto.*;
import com.example.hospital.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({ "/api/records", "/api/doctors/records", "/api/doctors/medical-records" })
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public MedicalRecordResponse write(@Valid @RequestBody MedicalRecordRequest req,
            Authentication auth) {
        return recordService.create(req, auth.getName());
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public List<MedicalRecordResponse> forPatient(@PathVariable Long patientId,
            Authentication auth) {
        return recordService.forPatient(patientId, auth.getName());
    }
}