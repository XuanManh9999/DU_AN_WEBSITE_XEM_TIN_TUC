package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.RoleEntity;
import com.datn.tran_luong.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findRoleEntityByName(Role name);
    Optional<RoleEntity> findByName(String name);

}