package com.datn.tran_luong.entity;

import com.datn.tran_luong.enums.Active;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity <T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "active")
    private Active active = Active.CHUA_HOAT_DONG;

    @Column(name = "create_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
}