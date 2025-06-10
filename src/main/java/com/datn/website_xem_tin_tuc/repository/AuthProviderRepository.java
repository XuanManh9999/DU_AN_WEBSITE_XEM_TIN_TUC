package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.AuthProvider;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    Optional<AuthProvider> findAuthProviderByUserAndActive(UserEntity user, Active active);
}