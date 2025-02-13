package com.ehr.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ehr.dto.UserDTO;
import com.ehr.exceptions.RequestedEntityNotFoundException;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @GetMapping("/details/{user-id}")
    public ResponseEntity<UserDTO> getById(
        @PathVariable("user-id") long userId
    ) throws RequestedEntityNotFoundException;

}
