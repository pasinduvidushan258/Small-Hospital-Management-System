package com.example.hospital.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record AppointmentRequest(
        @NotNull Long doctorId,
        @NotNull @Future LocalDateTime dateTime
) {}