package com.ehr.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ehr.clients.AppointmentServiceClient;
import com.ehr.clients.UserServiceClient;
import com.ehr.dto.AppointmentDTO;
import com.ehr.dto.UserResponse;
// import com.ehr.dto.response.UserResponse;
import com.ehr.entities.PatientRecordEntity;
// import com.ehr.entities.UserEntity;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.repositories.PatientRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final AppointmentServiceClient appointmentServiceClient;
    private final PatientRecordRepository patientRecordRepository;
    private final UserServiceClient userServiceClient;

    // private final RestTemplate userServiceRestTemplate;

    // private final RestTemplate appointmentServiceRestTemplate;


    public PatientRecordEntity getPatientRecord(long id) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = patientRecordRepository.findById(id)
            .orElseThrow(() -> new RequestedEntityNotFoundException("No Patient Record Found with id : " + id));

        return patientRecord;
    }

    public List<UserResponse> getAccessList(long id) throws RequestedEntityNotFoundException {

        PatientRecordEntity patientRecord = getPatientRecord(id);

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        
        // HttpEntity<Set<Long>> httpEntity = new HttpEntity<>(patientRecord.getAccessListUserIds(), headers);
        // ResponseEntity<Set<UserResponse>> exchange = userServiceRestTemplate.exchange("/all-details", HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Set<UserResponse>>() {});

        ResponseEntity<List<UserResponse>> exchange = userServiceClient.getAll(patientRecord.getAccessListUserIds());

        List<UserResponse> users = new ArrayList<>(exchange.getBody());

        return users;
    }

    @SuppressWarnings("null")
    public PatientRecordEntity addUserToAccessList(long patientId, String walletAddress) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = getPatientRecord(patientId);
        // ResponseEntity<UserResponse> user = userServiceRestTemplate.getForEntity("/wallet/{wallet-address}", UserResponse.class, walletAddress);
        ResponseEntity<UserResponse> user = userServiceClient.getByWallet(walletAddress);
        patientRecord.getAccessListUserIds().add(user.getBody().getId());

        return patientRecordRepository.save(patientRecord);
    }
    
    @SuppressWarnings("null")
    public PatientRecordEntity removeUserToAccessList(long patientId, String walletAddress) throws RequestedEntityNotFoundException {
        PatientRecordEntity patientRecord = getPatientRecord(patientId);
        // ResponseEntity<UserResponse> user = userServiceRestTemplate.getForEntity("/wallet/{wallet-address}", UserResponse.class, walletAddress);
        ResponseEntity<UserResponse> user = userServiceClient.getByWallet(walletAddress);
        patientRecord.getAccessListUserIds().remove(user.getBody().getId());

        return patientRecordRepository.save(patientRecord);
    }

    public List<AppointmentDTO> getAppointment(long userId) throws RequestedEntityNotFoundException {
        // ResponseEntity<List<AppointmentDTO>> exchange = appointmentServiceRestTemplate.exchange("/{user-id}/patient", HttpMethod.GET, null, new ParameterizedTypeReference<List<AppointmentDTO>>() {}, userId);
        ResponseEntity<List<AppointmentDTO>> exchange = appointmentServiceClient.getAppointmentByUser(userId);
        return exchange.getBody();
    }
 
    public List<UserResponse> getUser(Iterable<Long> ids) throws RequestedEntityNotFoundException {
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity<Iterable<Long>> httpEntity = new HttpEntity<>(ids, headers);
        ResponseEntity<List<UserResponse>> exchange = userServiceClient.getAll(ids);
        return exchange.getBody();
    }
    
    public UserResponse getUser(long id) throws RequestedEntityNotFoundException {
        // ResponseEntity<UserResponse> forEntity = userServiceRestTemplate.getForEntity("/details/{user-id}", UserResponse.class, id);
        ResponseEntity<UserResponse> forEntity = userServiceClient.getById(id);
        return forEntity.getBody();
    }
    
    public AppointmentDTO cancelAppointment(long id, String reason) throws RequestedEntityNotFoundException {
        // ResponseEntity<AppointmentDTO> forEntity = appointmentServiceRestTemplate.postForEntity("/{appointment-id}/cancel", reason, AppointmentDTO.class, id);
        ResponseEntity<AppointmentDTO> forEntity = appointmentServiceClient.cancelAppointment(id, reason);
        return forEntity.getBody();
    }

}
