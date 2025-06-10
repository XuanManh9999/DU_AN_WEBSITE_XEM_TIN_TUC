package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comments")
@Data
public class CommentEntity extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticlesEntity articles;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
}
