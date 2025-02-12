package com.ehr.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehr.dto.UserApplicationRequest;
import com.ehr.entities.UserApplicationEntity;
import com.ehr.exceptions.EntityAlreadyExistsException;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.services.UserApplicationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-application/apply")
@Tag(
    name = "User Application Controller",
    description = "All end points useful for users to apply for jobs" 
)
public class UserApplicationApplyController {

    private final UserApplicationService userApplicationService;

    @PostMapping
    public ResponseEntity<?> apply(
        @Valid @RequestBody UserApplicationRequest request
    ) throws EntityAlreadyExistsException {
        UserApplicationEntity application = userApplicationService.apply(request);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(
                    Map.of(
                        "message", "Application submitted successfully",
                        "application", application
                    )
                );
    }

    @GetMapping("/tracking/{trackingId}")
    public ResponseEntity<UserApplicationEntity> getApplicationByTrackingId(@PathVariable String trackingId) throws RequestedEntityNotFoundException {
        UserApplicationEntity userApplicationEntity = userApplicationService.getApplicationByTrackingId(trackingId);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(userApplicationEntity);
    }
}
