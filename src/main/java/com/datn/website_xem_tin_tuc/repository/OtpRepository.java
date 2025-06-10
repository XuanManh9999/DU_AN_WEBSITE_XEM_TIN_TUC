package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findOtpEntitiesByEmailAndOtpCode(String email, String otpCode);
}
