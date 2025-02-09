package com.ehr.services;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehr.dto.AppointmentRequest;
import com.ehr.dto.UserDTO;
import com.ehr.dto.UserResponse;
import com.ehr.entities.AppointmentEntity;
import com.ehr.enums.AppointmentStatus;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.repositories.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;

    @Qualifier(value = "user-service")
    private final RestTemplate restTemplate;

    public AppointmentEntity addAppointment(AppointmentRequest request) throws EntityAlreadyExistsException {

        ResponseEntity<UserDTO> doctor = restTemplate.getForEntity("/details/{user-id}", UserDTO.class, request.getDoctorId());
        if (doctor.getStatusCode().value() == HttpStatus.NOT_FOUND.value()) 
            throw new RuntimeException("Doctor not found.");

        if (appointmentRepository.existsByDoctorIdAndDateAndSlot(request.getDoctorId(), request.getSlot(), request.getDate())) 
            throw new EntityAlreadyExistsException("Doctor is not free in that slot");
        
        if (appointmentRepository.existsByPatientIdAndDateAndSlot(request.getPatientId(), request.getSlot(), request.getDate())) 
            throw new EntityAlreadyExistsException("you already have an anther appointment in same slot");

        AppointmentEntity appointment = new AppointmentEntity();
        BeanUtils.copyProperties(request, appointment);
        appointment.setStatus(AppointmentStatus.CREATED);

        return appointmentRepository.save(appointment);        
    }

    public AppointmentEntity getAppointment(long id) throws RequestedEntityNotFoundException {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RequestedEntityNotFoundException("Appointment record not found with id : " + id));
    }

    public AppointmentEntity changeStatus(AppointmentEntity entity, AppointmentStatus status) {
        entity.setStatus(status);

        return appointmentRepository.save(entity);
    }

    public AppointmentEntity changeStatus(AppointmentEntity entity, AppointmentStatus status, String reason) {
        entity.setStatus(status);
        entity.setReason(reason);
        
        return appointmentRepository.save(entity);
    }

    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<AppointmentEntity> getDoctorAppointment(long id) {
        return appointmentRepository.findByDoctorId(id);
    }
    
    public List<AppointmentEntity> getPatientAppointment(long id) {
        return appointmentRepository.findByPatientId(id);
    }

    public UserResponse getUser(long id) {
        ResponseEntity<UserResponse> forEntity = restTemplate.getForEntity("/details/{user-id}", UserResponse.class, id);
        return forEntity.getBody();
    }

}
