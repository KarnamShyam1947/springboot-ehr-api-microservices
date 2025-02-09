package com.ehr.repositories;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehr.entities.AppointmentEntity;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query(value = "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a WHERE a.patientId = :patientId AND a.date = :date AND a.slot = :slot")
    boolean existsByPatientIdAndDateAndSlot(
        @Param("patientId") long patientId,
        @Param("slot") String slot, 
        @Param("date") Date date
    );

    @Query(value = "SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AppointmentEntity a WHERE a.doctorId = :doctorId AND a.date = :date AND a.slot = :slot")
    boolean existsByDoctorIdAndDateAndSlot(
        @Param("doctorId") long doctorId,
        @Param("slot") String slot,
        @Param("date") Date date
    );

    List<AppointmentEntity> findByPatientId(long patientId);
    List<AppointmentEntity> findByDoctorId(long doctorId);

}
