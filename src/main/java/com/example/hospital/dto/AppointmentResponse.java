package com.example.hospital.dto;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        String patientName,
        String doctorName,
        LocalDateTime dateTime,
        String status
) {}