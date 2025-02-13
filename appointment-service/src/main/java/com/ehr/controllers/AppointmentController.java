package com.ehr.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ehr.dto.AppointmentRequest;
import com.ehr.dto.UserDTO;
import com.ehr.entities.AppointmentEntity;
import com.ehr.enums.AppointmentStatus;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.services.AppointmentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/appointment")
@Tag(
    name = "Appointment Controller",
    description = "All end points useful for user to manage their Appointments" 
)
public class AppointmentController {

    private final AppointmentService appointmentService;
    
    @GetMapping
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(appointmentService.getAllAppointments());
    }
    
    @GetMapping("/{appointment-id}")
    public ResponseEntity<?> getAppointment(
        @PathVariable("appointment-id") int appointmentId
    ) throws RequestedEntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(appointmentService.getAppointment(appointmentId));
    }
    
    @PostMapping("/{appointment-id}/cancel")
    public ResponseEntity<?> cancelAppointment(
        @PathVariable("appointment-id") long appointmentId,
        @RequestBody String reason
    ) throws RequestedEntityNotFoundException {
        AppointmentEntity appointment = appointmentService.getAppointment(appointmentId);
        UserDTO currentUser = appointmentService.getUser(appointment.getPatientId());
        if (currentUser.getId() != appointment.getPatientId()) 
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can't access other user appointment");

        if (appointment.getStatus().name().equals(AppointmentStatus.EXPIRED.name())) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The appointment schedule is complete");
    
        if (appointment.getStatus().name().equals(AppointmentStatus.REJECTED.name())) 
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already rejected the appointment can't preform the request action");
            
        
        return ResponseEntity
                .status(HttpStatus.ACCEPTED.value())
                .body(appointmentService.changeStatus(appointment, AppointmentStatus.CANCELED, reason));

    }
    
    @GetMapping("/{user-id}/patient")
    public ResponseEntity<?> getAppointmentByUser(
        @PathVariable("user-id") long userId
    ) throws RequestedEntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(appointmentService.getPatientAppointment(userId));
    }
    
    @GetMapping("/{user-id}/doctor")
    public ResponseEntity<?> getAppointmentByDoctor(
        @PathVariable("user-id") int userId
    ) throws RequestedEntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(appointmentService.getDoctorAppointment(userId));
    }

    @PostMapping
    public ResponseEntity<?> addAppointments(
        @Valid @RequestBody AppointmentRequest request
    ) throws EntityAlreadyExistsException, RequestedEntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(appointmentService.addAppointment(request));
    }

}
