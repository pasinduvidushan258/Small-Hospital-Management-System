package com.example.hospital.dto;

import java.time.LocalDateTime;

public record MedicalRecordResponse(
        Long id,
        String patientName,
        String doctorName,
        String diagnosis,
        String prescription,
        LocalDateTime createdAt
) {}