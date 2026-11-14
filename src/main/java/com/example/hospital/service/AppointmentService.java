package com.example.hospital.service;

import com.example.hospital.dto.*;
import com.example.hospital.exception.ApiException;
import com.example.hospital.model.*;
import com.example.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository apptRepo;
    private final UserRepository userRepo;
    private final PatientRepository patientRepo;

    public AppointmentResponse book(String patientUsername, AppointmentRequest req) {
        Patient patient = findPatientByUsername(patientUsername);

        AppUser doctor = userRepo.findById(req.doctorId())
                .orElseThrow(() -> new ApiException(404, "Doctor not found"));
        if (doctor.getRole() != Role.DOCTOR)
            throw new ApiException(400, "Selected user is not a doctor");

        Appointment a = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .dateTime(req.dateTime())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        return map(apptRepo.save(a));
    }


    public List<AppointmentResponse> listForDoctor(AppUser user) {
        if (user.getRole() == Role.ADMIN) {
            return apptRepo.findAll().stream().map(this::map).toList();
        }
        return apptRepo.findByDoctor(user).stream().map(this::map).toList();
    }

    public AppointmentResponse updateStatus(Long id, AppointmentStatus status, String doctorUsername) {
        Appointment a = apptRepo.findById(id)
                .orElseThrow(() -> new ApiException(404, "Appointment not found"));

        // Doctor or admin can update status
        AppUser caller = userRepo.findByUsername(doctorUsername).orElse(null);
        boolean isAdmin = caller != null && caller.getRole() == Role.ADMIN;
        if (!isAdmin && !a.getDoctor().getUsername().equals(doctorUsername))
            throw new ApiException(403, "Not your appointment");

        a.setStatus(status);
        return map(apptRepo.save(a));
    }

    private Patient findPatientByUsername(String username) {
        // Try finding by full name or username matching full name
        AppUser user = userRepo.findByUsername(username).orElse(null);
        String searchName = user != null ? user.getFullName() : username;

        return patientRepo.findByFullNameContainingIgnoreCase(searchName)
                .stream().findFirst()
                .orElseGet(() -> patientRepo.save(Patient.builder()
                        .fullName(searchName)
                        .email(username.toLowerCase().replace("/", "_") + "@hospital.com")
                        .phone("555-0199")
                        .dateOfBirth(LocalDate.of(2000, 1, 1))
                        .build()));
    }

    private AppointmentResponse map(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getFullName(),
                a.getDoctor().getFullName(),
                a.getDateTime(),
                a.getStatus().name());
    }
}