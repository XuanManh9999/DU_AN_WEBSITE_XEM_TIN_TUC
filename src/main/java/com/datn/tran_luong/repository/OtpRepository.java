package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findOtpEntitiesByEmailAndOtpCode(String email, String otpCode);
}
