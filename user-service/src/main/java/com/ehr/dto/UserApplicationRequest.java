package com.ehr.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class 
UserApplicationRequest {

    @NotBlank(message = "type is required")
    private String type;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "resumeUrl is required")
    private String resumeUrl;

    @DecimalMin(value = "1.0", message = "Minium experience is required")
    private double experience;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;

    @NotBlank(message = "walletAddress is required")
    private String walletAddress;

    @NotBlank(message = "specialization is required")
    private String specialization;
}
