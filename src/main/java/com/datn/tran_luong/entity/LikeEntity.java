package com.datn.tran_luong.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "likes")
@Data
public class LikeEntity extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "articles_id")
    private ArticlesEntity articles;
}
