package com.ehr.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ehr.dto.UserApplicationRequest;
import com.ehr.dto.UserRequest;
import com.ehr.dto.UserResponse;
import com.ehr.dto.VerifyHashRequest;
import com.ehr.entities.UserEntity;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.services.UserService;
import com.ehr.utils.BlockchainHashUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/details/{user-id}")
    public ResponseEntity<UserEntity> getById(
        @PathVariable("user-id") long userId
    ) throws RequestedEntityNotFoundException {
        UserEntity user = userService.getById(userId);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(user);
    }
    
    @GetMapping("/wallet/{wallet-address}")
    public ResponseEntity<UserEntity> getByWallet(
        @PathVariable("wallet-address") String walletAddress
    ) throws RequestedEntityNotFoundException {
        UserEntity user = userService.getByWalletAddress(walletAddress);

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(user);
    }

    @PostMapping("/all-details")
    public ResponseEntity<List<UserResponse>> getaAll(@RequestBody List<Long> iDs) throws RequestedEntityNotFoundException {
        System.out.println(iDs);
        List<UserResponse> all = userService.getAll(iDs);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(all);
    }

    @PostMapping("/add-from-application")
    public ResponseEntity<UserEntity> addUserFromApplication(@RequestBody UserApplicationRequest request) {
        UserEntity userFromApplication = userService.addUserFromApplication(request);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(userFromApplication);

    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(
        @RequestBody UserRequest request
    ) throws EntityAlreadyExistsException {
        System.out.println(request);
        UserEntity patient = userService.addPatient(request);
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(patient);
    } 

    @PostMapping("/verify-hash")
    public ResponseEntity<?> verifyHash(
        @RequestBody VerifyHashRequest request
    ) throws RequestedEntityNotFoundException {
        UserEntity user = userService.getByWalletAddress(request.getUserWalletAddress());
        boolean isValid = BlockchainHashUtils.verifyDataIntegrity("SHA-256", user, request.getStoredHash());

        if (!isValid) 
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The hash doesn't match");
        
        return ResponseEntity
                .status(HttpStatus.ACCEPTED.value())
                .body(user);

    }
    
    @PostMapping("/generate-hash")
    public ResponseEntity<?> generateHash(
        @RequestBody String userWalletAddress
    ) throws RequestedEntityNotFoundException, NoSuchAlgorithmException, IOException {
        UserEntity user = userService.getByWalletAddress(userWalletAddress);
        String hashObject = BlockchainHashUtils.hashObject(user, "SHA-256");

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(Map.of(
                    "hash", hashObject
                ));
    }
    
}
