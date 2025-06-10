package com.datn.website_xem_tin_tuc.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class CategoryEntity extends BaseEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<CategoryEntity> children;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ArticlesEntity> articlesEntities;
}
