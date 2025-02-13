package com.ehr.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ehr.dto.AppointmentDTO;
import com.ehr.exceptions.RequestedEntityNotFoundException;

@FeignClient(name = "APPOINTMENT-SERVICE")
public interface AppointmentServiceClient {

    @GetMapping("/api/v1/appointment/{user-id}/patient")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentByUser(@PathVariable("user-id") long userId) throws RequestedEntityNotFoundException;

    @PostMapping("/api/v1/appointment/{appointment-id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(
        @PathVariable("appointment-id") long appointmentId,
        @RequestBody String reason
    ) throws RequestedEntityNotFoundException;
    
}
