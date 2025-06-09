package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.UserEntity;
import com.datn.tran_luong.enums.Active;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameAndEmail(String username, String email);
    Optional<UserEntity> findUserEntityByEmailAndActive(String email, Active active);
}
