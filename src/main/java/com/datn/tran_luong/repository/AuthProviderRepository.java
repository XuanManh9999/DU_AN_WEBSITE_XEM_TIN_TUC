package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.AuthProvider;
import com.datn.tran_luong.entity.UserEntity;
import com.datn.tran_luong.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    Optional<AuthProvider> findAuthProviderByUserAndActive(UserEntity user, Active active);
}