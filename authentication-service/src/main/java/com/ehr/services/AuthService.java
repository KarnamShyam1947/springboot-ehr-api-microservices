package com.ehr.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ehr.dto.LoginRequest;
import com.ehr.dto.SetPasswordRequest;
import com.ehr.dto.UserRequest;
import com.ehr.entities.UserEntity;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.exceptions.TokenExpiredException;
import com.ehr.repositories.UserRepository;
import com.ehr.utils.Utils;

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

    public UserEntity setPassword(SetPasswordRequest request) throws RequestedEntityNotFoundException, TokenExpiredException, EntityAlreadyExistsException {
        UserEntity user = userRepository.findByToken(request.getToken())
                            .orElseThrow(() -> new RequestedEntityNotFoundException("Invalid link"));

        if (user.getExpireTime() == null || user.getExpireTime().before(new Date())) 
            throw new TokenExpiredException("The link is expired. please request again for a new link");

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setExpireTime(null);
        user.setToken(null);
        
        return userRepository.save(user);
    }

    public UserEntity sendVerificationEmail(String email) throws RequestedEntityNotFoundException {
        UserEntity byEmail = getByEmail(email);
        if(byEmail == null)
            throw new RequestedEntityNotFoundException();

        byEmail.setToken(Utils.generateRandomString(10));
        byEmail.setExpireTime(Utils.getAddedDate(1, Calendar.HOUR));

        return userRepository.save(byEmail);
    }

}