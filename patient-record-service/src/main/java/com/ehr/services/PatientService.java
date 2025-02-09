package com.ehr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehr.dto.AppointmentDTO;
import com.ehr.dto.UserResponse;
// import com.ehr.dto.response.UserResponse;
import com.ehr.entities.PatientRecordEntity;
// import com.ehr.entities.UserEntity;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.repositories.PatientRecordRepository;

@Service
public class PatientService {

    private final PatientRecordRepository patientRecordRepository;

    private final RestTemplate userServiceRestTemplate;

    private final RestTemplate appointmentServiceRestTemplate;

    public PatientService(
        PatientRecordRepository patientRecordRepository, 
        @Qualifier(value = "user-service") RestTemplate userServiceRestTemplate,
        @Qualifier(value = "appointment-service") RestTemplate appointmentServiceRestTemplate) {
        this.patientRecordRepository = patientRecordRepository;
        this.userServiceRestTemplate = userServiceRestTemplate;
        this.appointmentServiceRestTemplate = appointmentServiceRestTemplate;
    }

    public PatientRecordEntity getPatientRecord(long id) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = patientRecordRepository.findById(id)
            .orElseThrow(() -> new RequestedEntityNotFoundException("No Patient Record Found with id : " + id));

        return patientRecord;
    }

    public List<UserResponse> getAccessList(long id) throws RequestedEntityNotFoundException {

        PatientRecordEntity patientRecord = getPatientRecord(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Set<Long>> httpEntity = new HttpEntity<>(patientRecord.getAccessListUserIds(), headers);

        ResponseEntity<Set<UserResponse>> exchange = userServiceRestTemplate.exchange("/all-details", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Set<UserResponse>>() {});

        List<UserResponse> users = new ArrayList<>(exchange.getBody());

        return users;
    }

    @SuppressWarnings("null")
    public PatientRecordEntity addUserToAccessList(long patientId, String walletAddress) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = getPatientRecord(patientId);
        ResponseEntity<UserResponse> user = userServiceRestTemplate.getForEntity("/wallet/{wallet-address}", UserResponse.class, walletAddress);
        patientRecord.getAccessListUserIds().add(user.getBody().getId());

        return patientRecordRepository.save(patientRecord);
    }
    
    @SuppressWarnings("null")
    public PatientRecordEntity removeUserToAccessList(long patientId, String walletAddress) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = getPatientRecord(patientId);
        ResponseEntity<UserResponse> user = userServiceRestTemplate.getForEntity("/wallet/{wallet-address}", UserResponse.class, walletAddress);
        patientRecord.getAccessListUserIds().remove(user.getBody().getId());

        return patientRecordRepository.save(patientRecord);
    }

    public List<AppointmentDTO> getAppointment(long userId) {
        ResponseEntity<List<AppointmentDTO>> exchange = appointmentServiceRestTemplate.exchange("/{user-id}/patient", HttpMethod.GET, null, new ParameterizedTypeReference<List<AppointmentDTO>>() {}, userId);
        return exchange.getBody();
    }
 
    public List<UserResponse> getUser(Iterable<Long> ids) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Iterable<Long>> httpEntity = new HttpEntity<>(ids, headers);
        ResponseEntity<List<UserResponse>> exchange = userServiceRestTemplate.exchange("/all-details", HttpMethod.POST, httpEntity, new ParameterizedTypeReference<List<UserResponse>>() {});
        return exchange.getBody();
    }
    
    public UserResponse getUser(long id) {
        ResponseEntity<UserResponse> forEntity = userServiceRestTemplate.getForEntity("/details/{user-id}", UserResponse.class, id);
        return forEntity.getBody();
    }
    
    public AppointmentDTO cancelAppointment(long id, String reason) {
        ResponseEntity<AppointmentDTO> forEntity = appointmentServiceRestTemplate.postForEntity("/{appointment-id}/cancel", reason, AppointmentDTO.class, id);
        return forEntity.getBody();
    }

}
