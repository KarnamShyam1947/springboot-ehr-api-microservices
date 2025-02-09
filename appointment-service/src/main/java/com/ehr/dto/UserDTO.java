package com.ehr.dto;

import java.util.Date;

import com.ehr.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private long id;
    private Role role;
    private String email;
    private String token;
    private String address;
    private Date expireTime;
    private String username;
    private String resumeUrl;
    private double experience;
    private String phoneNumber;
    private String walletAddress;
    private String specialization;
}
