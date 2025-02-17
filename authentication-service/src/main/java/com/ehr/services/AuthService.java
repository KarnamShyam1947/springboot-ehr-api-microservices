package com.ehr.services;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ehr.dto.LoginRequest;
import com.ehr.dto.UserRequest;
import com.ehr.entities.UserEntity;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity getByEmail(String email) {
        UserEntity byEmail = userRepository.findByEmail(email);
        
        return byEmail;
    }

    public UserEntity register(UserRequest request) throws EntityAlreadyExistsException {

        UserEntity byEmail = getByEmail(request.getEmail());
        if(byEmail != null){
            throw new EntityAlreadyExistsException("User Already exists with same email id");
        }

        UserEntity user = new UserEntity();
        BeanUtils.copyProperties(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }
    
    public UserEntity login(LoginRequest request) throws RequestedEntityNotFoundException {

        UserEntity byEmail = getByEmail(request.getEmail());
        if(byEmail == null)
            throw new RequestedEntityNotFoundException();

        if (!passwordEncoder.matches(request.getPassword(), byEmail.getPassword())) 
            throw new BadCredentialsException("Invalid user credentials");


        return byEmail;
    }

}