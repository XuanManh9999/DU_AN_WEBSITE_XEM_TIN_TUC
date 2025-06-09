package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.RoleEntity;
import com.datn.tran_luong.entity.UserEntity;
import com.datn.tran_luong.entity.UserRoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRoleEntity ur WHERE ur.userId.id = :userId")
    void deleteUserRolesByUserId(Long userId);
    Optional<UserRoleEntity> findUserRoleEntitiesByUserIdAndRoleId(UserEntity user, RoleEntity role);
}