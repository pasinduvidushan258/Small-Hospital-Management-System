package com.example.hospital.dto;

import com.example.hospital.model.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull AppointmentStatus status) {}