package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.RoleEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.entity.UserRoleEntity;
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