package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookmarks")
@Data
public class BookmarkEntity extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "articles_id")
    private ArticlesEntity articles;

}