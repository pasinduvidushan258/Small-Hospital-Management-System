package com.example.hospital.repository;

import com.example.hospital.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctor(AppUser doctor);
    List<Appointment> findByPatient(Patient patient);
    boolean existsByPatient(Patient patient);
}