package com.example.hospital.service;

import com.example.hospital.exception.ApiException;
import com.example.hospital.model.Patient;
import com.example.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepo;
    private final AppointmentRepository apptRepo;
    private final MedicalRecordRepository recordRepo;

    public List<Patient> list() { return patientRepo.findAll(); }

    public Patient create(Patient p) { return patientRepo.save(p); }

    public void delete(Long id) {
        Patient p = patientRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "Patient not found"));

        // Business rule: block deletion if patient has appointments or records
        if (apptRepo.existsByPatient(p) || recordRepo.existsByPatient(p))
            throw new ApiException(409, "Cannot delete patient with existing data");

        patientRepo.delete(p);
    }
}