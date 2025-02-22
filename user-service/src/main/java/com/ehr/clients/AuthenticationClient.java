package com.ehr.clients;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ehr.dto.request.CreateUserRequest;
import com.ehr.exceptions.EntityAlreadyExistsException;

@FeignClient(name = "AUTHENTICATION-SERVICE")
public interface AuthenticationClient {
    
    @PostMapping("/api/v1/auth/register")
    public ResponseEntity<Map<String, Object>> register(
        @RequestBody CreateUserRequest userRequest
    ) throws EntityAlreadyExistsException;
}
