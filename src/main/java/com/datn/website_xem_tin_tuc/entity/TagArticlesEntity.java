package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tagArticles")
@Data
public class TagArticlesEntity extends BaseEntity<Long>{
    @ManyToOne
    @JoinColumn(name = "article_id")
    private ArticlesEntity articles;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private TagEntity tag;


}
