package com.ehr.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehr.entities.UserEntity;
import java.util.List;
import com.ehr.enums.Role;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    List<UserEntity> findByRole(Role role);
    Optional<UserEntity> findByToken(String token);
    UserEntity findByPhoneNumber(String phoneNumber);
    UserEntity findByWalletAddress(String walletAddress);
}
