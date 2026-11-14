package com.example.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank
    private String phone;

    @NotNull @Past
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // optional convenience
}