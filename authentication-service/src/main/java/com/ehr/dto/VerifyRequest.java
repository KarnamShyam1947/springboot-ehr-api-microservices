package com.ehr.dto;

import com.ehr.entities.UserEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyRequest {
    private String email;
    private UserEntity userEntity; 
}
