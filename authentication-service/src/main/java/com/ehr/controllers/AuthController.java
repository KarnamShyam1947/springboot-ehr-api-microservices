package com.ehr.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehr.config.custom.MyUserDetails;
import com.ehr.dto.LoginRequest;
import com.ehr.dto.SetPasswordRequest;
import com.ehr.dto.UserRequest;
import com.ehr.dto.VerifyRequest;
import com.ehr.entities.UserEntity;
import com.ehr.exceptions.BadRequestException;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.exceptions.TokenExpiredException;
import com.ehr.services.AuthService;
import com.ehr.services.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    
    @GetMapping
    public ResponseEntity<Map<String, String>> check() {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of("hello", "world")
                );
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
        @RequestBody LoginRequest loginRequest
    ) throws RequestedEntityNotFoundException {

        UserEntity login = authService.login(loginRequest);
        String jwtToken = jwtService.generateJwtToken(new MyUserDetails(login));

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of(
                        "message", "User logged in successfully",
                        "token", jwtToken,
                        "user", login
                    )
                );
    }
   
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
        @RequestBody UserRequest userRequest
    ) throws EntityAlreadyExistsException {

        UserEntity register = authService.register(userRequest);
        String jwtToken = jwtService.generateJwtToken(new MyUserDetails(register));
        
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(
                    Map.of(
                        "message", "User registered in successfully",
                        "token", jwtToken,
                        "user", register
                    )
                );
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<Map<String, Object>> sendVerificationEmail(
        @RequestBody Map<String, Object> requestMap
    ) throws BadRequestException, RequestedEntityNotFoundException {
        String email = (String)requestMap.get("email");

        if (email == null)
            throw new BadRequestException("Unable to find email in request body");

        UserEntity sendVerificationEmail = authService.sendVerificationEmail(email);
        return ResponseEntity
                .status(200)
                .body(Map.of(
                    "message", "mail sent successfully",
                    "user", sendVerificationEmail
                ));
    }
    
    @PostMapping("/set-password")
    public ResponseEntity<Map<String, Object>> setPassword(
        @RequestBody SetPasswordRequest request
    ) throws BadRequestException, RequestedEntityNotFoundException, TokenExpiredException, EntityAlreadyExistsException {

        UserEntity setPassword = authService.setPassword(request);

        return ResponseEntity
                .status(200)
                .body(Map.of(
                    "message", "mail sent successfully",
                    "user", setPassword
                ));
    }

    @PostMapping("/generate-token")
    public ResponseEntity<Map<String, Object>> generateToken(
        @RequestBody Map<String, Object> requestMap
    ) throws BadRequestException {
        String email = (String)requestMap.get("email");

        if (email == null)
            throw new BadRequestException("Unable to find email in request body");

        String jwtToken = jwtService.generateJwtToken(email);
        return ResponseEntity
                .status(200)
                .body(Map.of("token", jwtToken));
    }
   
    @PostMapping("/get-subject")
    public ResponseEntity<Map<String, Object>> getUsername(
        @RequestBody Map<String, Object> requestMap
    ) throws BadRequestException {
        String token = (String)requestMap.get("token");

        if (token == null)
            throw new BadRequestException("Unable to find token in request body");

        String subject = jwtService.getUsername(token);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(Map.of("subject", subject));
    }
    
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(
        @RequestBody VerifyRequest request
    ) throws BadRequestException {

        boolean status = jwtService.isValidToken(request.getEmail(), new MyUserDetails(request.getUserEntity()));
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(Map.of("status", status));
    }

}
