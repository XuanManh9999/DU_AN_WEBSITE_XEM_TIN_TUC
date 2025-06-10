package com.datn.website_xem_tin_tuc.entity;

import com.datn.website_xem_tin_tuc.enums.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "role")
public class RoleEntity extends BaseEntity<Integer> {
    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true, nullable = false)
    private Role name;
    @Column(name = "desc_role")
    private String descRole;
    @OneToMany(mappedBy = "roleId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoleEntity> userRoles;
}