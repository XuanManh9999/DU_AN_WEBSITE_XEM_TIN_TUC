package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.RoleEntity;
import com.datn.website_xem_tin_tuc.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findRoleEntityByName(Role name);
    Optional<RoleEntity> findByName(String name);

}