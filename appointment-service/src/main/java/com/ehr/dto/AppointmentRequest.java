package com.ehr.dto;

import java.util.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @Future(message = "Select a future date")
    private Date date;

    @NotBlank(message = "slot is required")
    private String slot;

    @NotBlank(message = "reason is required")
    private String reason;
    
    @Min(value = 0, message = "doctor id is required")
    private long doctorId;

    @Min(value = 0, message = "patient id is required")
    private long patientId;
}
