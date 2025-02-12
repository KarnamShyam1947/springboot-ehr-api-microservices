package com.ehr.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private long id;
    private Date date;
    private String slot;
    private String status;
    private String reason;
    private long doctorId;
    private long patientId;
    private String rejectionReason;
}