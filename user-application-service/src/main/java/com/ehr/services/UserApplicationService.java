package com.ehr.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ehr.dto.UserApplicationRequest;
import com.ehr.dto.UserDTO;
import com.ehr.entities.UserApplicationEntity;
import com.ehr.enums.ApplicationStatus;
import com.ehr.enums.ApplicationType;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.repositories.UserApplicationRepository;
import com.ehr.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserApplicationRepository userApplicationRepository;

    @Qualifier(value = "user-service")
    private final RestTemplate restTemplate;

    public UserApplicationEntity addUser(long applicationId) throws RequestedEntityNotFoundException {
        UserApplicationEntity user = userApplicationRepository.findById(applicationId).orElseThrow(() -> new RequestedEntityNotFoundException());
        System.out.println("in service : " + user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserApplicationEntity> requestEntity = new HttpEntity<>(user, headers);

        ResponseEntity<UserDTO> exchange = restTemplate.exchange("/add-from-application", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<UserDTO>() {});
        System.out.println(exchange.getBody());
        if (exchange.getStatusCode().value() == HttpStatus.CREATED.value()) {
            return user;
        }
        return null;
    }

    public UserApplicationEntity getById(long id) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = userApplicationRepository.findById(id)
                .orElseThrow(() -> new RequestedEntityNotFoundException("Application not found with id: " + id));

        return userApplicationEntity;

    }

    public UserApplicationEntity apply(UserApplicationRequest userApplicationRequest) throws EntityAlreadyExistsException {
        Optional<UserApplicationEntity> optional =  userApplicationRepository.findByEmail(userApplicationRequest.getEmail());
        
        if (optional.isPresent()) 
            throw new EntityAlreadyExistsException("Application already exists with email: " + userApplicationRequest.getEmail() + ". tracking id is : " + optional.get().getTrackingId());
        
        Optional<UserApplicationEntity> wallet =  userApplicationRepository.findByWalletAddress(userApplicationRequest.getWalletAddress());
        if (wallet.isPresent()) 
            throw new EntityAlreadyExistsException("Application already exists with wallet address: " + userApplicationRequest.getWalletAddress() + ". tracking id is : " + optional.get().getTrackingId());

        UserApplicationEntity userApplicationEntity = new UserApplicationEntity();

        BeanUtils.copyProperties(userApplicationRequest, userApplicationEntity);
        userApplicationEntity.setStatus(ApplicationStatus.APPLIED);
        userApplicationEntity.setTrackingId(Utils.generateRandomString(10));
        userApplicationEntity.setType(ApplicationType.valueOf(userApplicationRequest.getType()));

        return userApplicationRepository.save(userApplicationEntity);
    }
    
    public UserApplicationEntity updateApplicationStatus(long id, ApplicationStatus applicationStatus) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = getById(id);

        userApplicationEntity.setStatus(applicationStatus);
        return userApplicationRepository.save(userApplicationEntity);
    }

    public UserApplicationEntity updateApplicationStatus(UserApplicationEntity user, ApplicationStatus applicationStatus) throws RequestedEntityNotFoundException {
        user.setStatus(applicationStatus);
        return userApplicationRepository.save(user);
    }

    public UserApplicationEntity getApplicationByTrackingId(String trackingId) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = userApplicationRepository.findByTrackingId(trackingId)
                .orElseThrow(() -> new RequestedEntityNotFoundException("Application not found with tracking id: " + trackingId));

        return userApplicationEntity;
    }

    public UserApplicationEntity getApplicationById(long id) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = getById(id);

        if (userApplicationEntity.getStatus().name().equals(ApplicationStatus.APPLIED.name()))
            updateApplicationStatus(id, ApplicationStatus.REVIEWED);

        return userApplicationEntity;
    }

    public List<UserApplicationEntity> getApplicationsByType(ApplicationType type) {
        return userApplicationRepository.findByType(type);
    }

    public List<UserApplicationEntity> getApplicationsByStatus(ApplicationStatus status) {
        return userApplicationRepository.findByStatus(status);
    }

    public void deleteApplication(long id) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = getById(id);
        
        userApplicationRepository.delete(userApplicationEntity);
    }
}
