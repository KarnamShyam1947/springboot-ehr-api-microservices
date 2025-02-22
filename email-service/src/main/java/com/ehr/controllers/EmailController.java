package com.ehr.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehr.dto.SendEmailRequest;
import com.ehr.services.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;
    
    @PostMapping
    public ResponseEntity<Map<String, String>> sendEmail(
        @RequestBody SendEmailRequest request
    ) {
        emailService.sendEmail(request);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(Map.of(
                    "message", "mail send successfully"
                ));
    }

}
