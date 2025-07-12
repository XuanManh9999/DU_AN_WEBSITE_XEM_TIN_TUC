package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tag")
@Data
public class TagEntity extends BaseEntity<Integer>{
    private String name;
    private String description;

    @OneToMany(mappedBy = "tag")
    private List<TagArticlesEntity> tagArticlesEntities;

}
