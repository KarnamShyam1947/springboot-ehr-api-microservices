package com.ehr.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private long id;
    private Date date;
    private String slot;
    private String status;
    private String reason;
    private UserResponse doctor;
    private UserResponse patient;
    private String rejectionReason;
}
