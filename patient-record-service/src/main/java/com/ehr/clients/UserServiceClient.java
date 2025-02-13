package com.ehr.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ehr.dto.UserResponse;
import com.ehr.exceptions.RequestedEntityNotFoundException;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {
    
    @PostMapping("/api/v1/user/all-details")
    public ResponseEntity<List<UserResponse>> getAll(@RequestBody Iterable<Long> iDs) throws RequestedEntityNotFoundException;

    @GetMapping("/api/v1/user/wallet/{wallet-address}")
    public ResponseEntity<UserResponse> getByWallet(@PathVariable("wallet-address") String walletAddress) throws RequestedEntityNotFoundException;

    @GetMapping("/api/v1/user/details/{user-id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("user-id") long userId) throws RequestedEntityNotFoundException;

}
