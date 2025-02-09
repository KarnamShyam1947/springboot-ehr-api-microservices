package com.ehr.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ehr.dto.AppointmentDTO;
import com.ehr.dto.AppointmentResponse;
import com.ehr.dto.UserResponse;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.services.PatientService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/patient")
@Tag(
    name = "Patient Controller",
    description = "All end points useful for patients" 
)
public class PatientController {

    private final PatientService patientService;
    // private final AppointmentService appointmentService;
    
    @GetMapping("/{patient-id}/appointment")
    public ResponseEntity<?> appointments(
        @PathVariable("patient-id") long patientId
    ) {
        List<AppointmentDTO> patientAppointments = patientService.getAppointment(patientId);
        List<Long> doctorIds = patientAppointments.stream().map(a -> a.getDoctorId()).collect(Collectors.toList());
        doctorIds.add(patientId);
        System.out.println(doctorIds);
        List<UserResponse> doctorRecords = patientService.getUser(doctorIds);
        System.out.println("I am here : " + doctorRecords);

        UserResponse currentUser = doctorRecords.stream().filter(d -> d.getId() == patientId).findFirst().get();

        List<AppointmentResponse> appointments = patientAppointments.stream().map(a -> {
            AppointmentResponse response = new AppointmentResponse();
            BeanUtils.copyProperties(a, response);
            response.setStatus(a.getStatus());
            response.setPatient(currentUser);
            response.setDoctor(
                doctorRecords.stream()
                    .filter(p -> p.getId() == a.getDoctorId())
                    .findFirst().get()
            );
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(appointments);
    }

    @GetMapping("/{patient-id}/appointment/{appointment-id}/cancel")
    public ResponseEntity<AppointmentDTO> reject(
        @PathVariable("patient-id") long patientId,
        @PathVariable("appointment-id") long appointmentId,
        @RequestParam(required = true) String reason
    ) throws RequestedEntityNotFoundException {
        AppointmentDTO cancelAppointment = patientService.cancelAppointment(appointmentId, reason);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(cancelAppointment);
    }

    @GetMapping("/{patient-id}/access-list")
    public ResponseEntity<List<UserResponse>> accessList(
        @PathVariable("patient-id") long patientId
    ) throws RequestedEntityNotFoundException {
        List<UserResponse> accessList = patientService.getAccessList(patientId);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(accessList);
    }
    
    @PostMapping("/{patient-id}/access-list/add")
    public ResponseEntity<Map<String, String>> addToAccessList(
        @PathVariable("patient-id") int patientId,
        @RequestBody String walletAddress
    ) throws RequestedEntityNotFoundException {
        patientService.addUserToAccessList(patientId, walletAddress);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(Map.of("message", "user added successfully"));
    }
    
    @PostMapping("/{patient-id}/access-list/remove")
    public ResponseEntity<Map<String, String>> removeToAccessList(
        @PathVariable("patient-id") int patientId,
        @RequestBody String walletAddress
    ) throws RequestedEntityNotFoundException {
        patientService.removeUserToAccessList(patientId, walletAddress);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(Map.of("message", "user removed successfully"));
    }

}
