package com.ehr.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyHashRequest {

    @NotBlank(message = "userWalletAddress is required")
    private String userWalletAddress;
    
    @NotBlank(message = "storedHash is required")
    private String storedHash;
}
