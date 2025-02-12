package com.ehr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;

    @NotBlank(message = "walletAddress is required")
    private String walletAddress;
}
