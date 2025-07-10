package com.datn.website_xem_tin_tuc.entity;

import com.datn.website_xem_tin_tuc.enums.TypeArticles;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "articles")
@Data
public class ArticlesEntity extends BaseEntity<Long> {
    @Column(name = "title")
    private String title;
    @Column(name = "slug")
    private String slug;

    @Column(name = "type")
    private TypeArticles type;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "views")
    private Integer view;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "articles", cascade = CascadeType.ALL)
    private List<LikeEntity> likeEntityList;

    @OneToMany(mappedBy = "articles", cascade = CascadeType.ALL)
    private List<BookmarkEntity> bookmarkEntities;

    @OneToMany(mappedBy = "articles", cascade = CascadeType.ALL)
    private List<DocumentEntity> documentEntities;
}
