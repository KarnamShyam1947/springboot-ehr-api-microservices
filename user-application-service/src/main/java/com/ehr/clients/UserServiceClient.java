package com.ehr.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ehr.dto.UserDTO;
import com.ehr.entities.UserApplicationEntity;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/api/v1/user/add-from-application")
    public ResponseEntity<UserDTO> addUserFromApplication(@RequestBody UserApplicationEntity request);

}
