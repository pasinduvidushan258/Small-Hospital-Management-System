package com.example.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_records")
@Data 
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Patient patient;

    @ManyToOne(optional = false)
    private AppUser doctor;

    private String diagnosis;
    private String prescription;
    private LocalDateTime createdAt;
}