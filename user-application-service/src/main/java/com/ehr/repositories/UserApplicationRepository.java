package com.ehr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehr.entities.UserApplicationEntity;
import java.util.List;

import com.ehr.enums.ApplicationStatus;
import com.ehr.enums.ApplicationType;

@Repository
public interface UserApplicationRepository extends JpaRepository<UserApplicationEntity, Long> {
    Optional<UserApplicationEntity> findByEmail(String email);
    List<UserApplicationEntity> findByType(ApplicationType type);
    List<UserApplicationEntity> findByStatus(ApplicationStatus status);
    Optional<UserApplicationEntity> findByTrackingId(String trackingId);
    Optional<UserApplicationEntity> findByWalletAddress(String walletAddress);
    Optional<UserApplicationEntity> findById(long id);
}
