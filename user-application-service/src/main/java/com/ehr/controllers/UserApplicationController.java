package com.ehr.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ehr.entities.UserApplicationEntity;
import com.ehr.enums.ApplicationStatus;
import com.ehr.enums.ApplicationType;
import com.ehr.exceptions.RequestedEntityNotFoundException;
import com.ehr.services.UserApplicationService;
// import com.ehr.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-application")
@Tag(
    name = "Admin's Application Controller",
    description = "All end points useful for admin to manage user applications" 
)
public class UserApplicationController {

    private final UserApplicationService userApplicationService;
    // private final UserService userService;
    
    @GetMapping("/doctor")
    public ResponseEntity<?> doctor() {
        List<UserApplicationEntity> applications = userApplicationService.getApplicationsByType(ApplicationType.DOCTOR);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(applications);
    }
    
    @GetMapping("/lab-technician")
    public ResponseEntity<?> labTechnician() {
        List<UserApplicationEntity> applications = userApplicationService.getApplicationsByType(ApplicationType.LAB_TECHNICIAN);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(applications);
    }
    
    @GetMapping("/details/{application-id}")
    public ResponseEntity<UserApplicationEntity> getApplication(
        @PathVariable("application-id") long applicationId
    ) throws RequestedEntityNotFoundException {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(userApplicationService.getApplicationById(applicationId));
    }

    @GetMapping("/accept/{applicationId}")
    public ResponseEntity<?> accept(@PathVariable long applicationId) throws RequestedEntityNotFoundException {
        UserApplicationEntity user = userApplicationService.addUser(applicationId);

        if(user == null) 
            throw new RuntimeException("Error while doing operation.");

        UserApplicationEntity application = userApplicationService.updateApplicationStatus(user, ApplicationStatus.ACCEPTED);      
        
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(application);
    }

    @GetMapping("/reject/{applicationId}")
    public ResponseEntity<?> reject(@PathVariable long applicationId) throws RequestedEntityNotFoundException {
        UserApplicationEntity application = userApplicationService.updateApplicationStatus(applicationId, ApplicationStatus.REJECTED);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(application);
    }

    @DeleteMapping("/delete/{applicationId}")
    public ResponseEntity<?> delete(@PathVariable long applicationId) throws RequestedEntityNotFoundException {
        userApplicationService.deleteApplication(applicationId);
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(Map.of("message", "Application deleted successfully"));
    }

}
