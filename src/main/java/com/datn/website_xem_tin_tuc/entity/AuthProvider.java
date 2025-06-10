package com.datn.website_xem_tin_tuc.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class AuthProvider extends BaseEntity<Long> {
    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "provider_code")
    private String providerCode;

    @ManyToOne
    private UserEntity user;
}