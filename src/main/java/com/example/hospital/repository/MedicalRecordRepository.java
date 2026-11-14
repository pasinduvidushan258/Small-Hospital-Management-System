package com.example.hospital.repository;

import com.example.hospital.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPatient(Patient patient);
    boolean existsByPatient(Patient patient);
}