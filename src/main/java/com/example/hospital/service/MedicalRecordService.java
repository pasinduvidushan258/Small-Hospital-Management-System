package com.example.hospital.service;

import com.example.hospital.dto.*;
import com.example.hospital.exception.ApiException;
import com.example.hospital.model.*;
import com.example.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository recordRepo;
    private final PatientRepository patientRepo;
    private final UserRepository userRepo;

    public MedicalRecordResponse create(MedicalRecordRequest req, String doctorUsername) {
        AppUser doctor = userRepo.findByUsername(doctorUsername)
                .orElseThrow(() -> new ApiException(404, "Doctor not found"));

        Patient patient = patientRepo.findById(req.patientId())
                .orElseThrow(() -> new ApiException(404, "Patient not found"));

        MedicalRecord r = MedicalRecord.builder()
                .patient(patient)
                .doctor(doctor)
                .diagnosis(req.diagnosis())
                .prescription(req.prescription())
                .createdAt(LocalDateTime.now())
                .build();

        return map(recordRepo.save(r));
    }

    public List<MedicalRecordResponse> forPatient(Long patientId, String doctorUsername) {
        Patient p = patientRepo.findById(patientId)
                .orElseThrow(() -> new ApiException(404, "Patient not found"));
        return recordRepo.findByPatient(p).stream().map(this::map).toList();
    }

    private MedicalRecordResponse map(MedicalRecord r) {
        return new MedicalRecordResponse(
                r.getId(), r.getPatient().getFullName(), r.getDoctor().getFullName(),
                r.getDiagnosis(), r.getPrescription(), r.getCreatedAt());
    }
}