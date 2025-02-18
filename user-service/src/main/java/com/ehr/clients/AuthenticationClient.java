package com.ehr.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ehr.dto.request.CreateUserRequest;
import com.ehr.exceptions.EntityAlreadyExistsException;

@FeignClient(name = "USER-SERVICE")
public interface AuthenticationClient {
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(
        @RequestBody CreateUserRequest userRequest
    ) throws EntityAlreadyExistsException;
}
