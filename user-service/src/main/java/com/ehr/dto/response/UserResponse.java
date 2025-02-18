package com.ehr.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;
    private String role;
    private String email;
    private String address;
    private String username;
    private String resumeUrl;
    private double experience;
    private String phoneNumber;
    private String walletAddress;
    private String specialization;

}
