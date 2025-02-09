package com.ehr.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_records")
public class PatientRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "prescription", columnDefinition = "TEXT")
    private String prescription;

    @ElementCollection
    @CollectionTable(
        name = "patient_record_access_list",
        joinColumns = @JoinColumn(name = "patient_record_id")
    )
    @Column(name = "access_user_id")
    private Set<Long> accessListUserIds;
    
    @ElementCollection
    @CollectionTable(
        name = "patient_appointments",
        joinColumns = @JoinColumn(name = "patient_record_id")
    )
    @Column(name = "appointment_id")
    private Set<Long> appointmentIds;

    private long doctorId;
    private long patientId;

}
