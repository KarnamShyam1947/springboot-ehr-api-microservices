package com.ehr.entities;

import com.ehr.enums.Role;

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
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String email;
    private String username;
    private String resumeUrl;
    private double experience;
    private String phoneNumber;
    private String walletAddress;
    private String specialization;
}
