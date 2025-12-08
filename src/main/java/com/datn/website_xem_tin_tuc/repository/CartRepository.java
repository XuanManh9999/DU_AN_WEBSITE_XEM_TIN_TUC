package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.CartEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByUser(UserEntity user);
    Optional<CartEntity> findByUser_Id(Long userId);
}

