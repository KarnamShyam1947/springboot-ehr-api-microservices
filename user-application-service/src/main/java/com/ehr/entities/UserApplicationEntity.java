package com.ehr.entities;

import com.ehr.enums.ApplicationStatus;
import com.ehr.enums.ApplicationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_applications")
public class UserApplicationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Enumerated(value = EnumType.STRING)
    private ApplicationType type;
    
    @Enumerated(value = EnumType.STRING)
    private ApplicationStatus status;

    private String email;
    private String username;
    private String resumeUrl;
    private double experience;
    private String trackingId;
    private String phoneNumber;
    private String walletAddress;
    private String specialization;
}
