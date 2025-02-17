package com.ehr.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ehr.clients.AuthenticationClient;
import com.ehr.dto.request.CreateUserRequest;
import com.ehr.dto.request.SetPasswordRequest;
import com.ehr.dto.request.UserApplicationRequest;
import com.ehr.dto.request.UserRequest;
import com.ehr.dto.response.UserResponse;
import com.ehr.entities.UserEntity;
import com.ehr.enums.ApplicationType;
import com.ehr.enums.Role;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.exceptions.TokenExpiredException;
import com.ehr.repositories.UserRepository;
import com.ehr.utils.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationClient authenticationClient;

    public UserEntity getById(long id) throws RequestedEntityNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new RequestedEntityNotFoundException("User not found with id : " + id));
    }

    public UserEntity addUserFromApplication(UserApplicationRequest request) {
        UserEntity user = new UserEntity();
        
        BeanUtils.copyProperties(request, user);
        
        user.setId(0);
        if(request.getType().equals(ApplicationType.DOCTOR.name()))
            user.setRole(Role.DOCTOR);
        
        if(request.getType().equals(ApplicationType.LAB_TECHNICIAN.name()))
            user.setRole(Role.LAD_TECHNICIAN);

        user.setToken(UUID.randomUUID().toString());
        user.setExpireTime(Utils.getAddedDate(1, Calendar.HOUR));
        // TODO: main servive send link
        
        return userRepository.save(user);
    }

    public UserEntity addPatient(UserRequest request) throws EntityAlreadyExistsException {

        UserEntity byEmail = userRepository.findByEmail(request.getEmail());
        if (byEmail != null) 
            throw new EntityAlreadyExistsException("User already exists with same email : " + request.getEmail());
        
        UserEntity userWall = userRepository.findByWalletAddress(request.getWalletAddress());
        if (userWall != null)
            throw new EntityAlreadyExistsException("User already exists with same wallet address : " + request.getWalletAddress());

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(request, user);
        user.setRole(Role.PATIENT);

        ResponseEntity<Map<String, Object>> register = authenticationClient.register(
            new CreateUserRequest("PATIENT", request.getEmail(), request.getPassword())
        );
        System.out.println(register);

        return userRepository.save(user);
    }

    // TODO : Move to authentication service
    public UserEntity setPassword(SetPasswordRequest request) throws RequestedEntityNotFoundException, TokenExpiredException, EntityAlreadyExistsException {
        UserEntity user = userRepository.findByToken(request.getToken())
                            .orElseThrow(() -> new RequestedEntityNotFoundException("Invalid link"));

        if (user.getExpireTime() == null || user.getExpireTime().before(new Date())) 
            throw new TokenExpiredException("The link is expired. please request again for a new link");

        authenticationClient.register(new CreateUserRequest(
            user.getRole().name(),
            user.getEmail(),
            request.getPassword()
        ));

        user.setToken(null);
        user.setExpireTime(null);
        
        return userRepository.save(user);
    }

    public List<UserEntity> getPatients() {
        return userRepository.findByRole(Role.PATIENT);
    }
    
    public List<UserEntity> getDoctors() {
        return userRepository.findByRole(Role.DOCTOR);
    }
    
    public List<UserEntity> getLabTech() {
        return userRepository.findByRole(Role.LAD_TECHNICIAN);
    }
    
    public List<UserEntity> getAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }
    
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }
    
    public List<UserResponse> getAll(Iterable<Long> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .map(u -> getUserResponse(u))
                .collect(Collectors.toList());
    }

    public UserEntity getByWalletAddress(String walletAddress) throws RequestedEntityNotFoundException {
        System.out.println("in service class : " + walletAddress);

        UserEntity byWalletAddress = userRepository.findByWalletAddress(walletAddress);
        if (byWalletAddress == null) 
            throw new RequestedEntityNotFoundException("User doesn't exists with wallet address : " + walletAddress);
        
        return byWalletAddress;
    }

    private UserResponse getUserResponse(UserEntity u) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(u, response);
        response.setRole(u.getRole().name());
        
        return response;
    }
    

}
