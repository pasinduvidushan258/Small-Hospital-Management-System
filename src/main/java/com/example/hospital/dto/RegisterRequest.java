package com.example.hospital.dto;

import com.example.hospital.model.Role;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;

public record RegisterRequest(
                @NotBlank String username,
                @NotBlank String password,
                @NotBlank @JsonAlias({ "name", "fullname" }) String fullName,
                @NotNull Role role) {
}