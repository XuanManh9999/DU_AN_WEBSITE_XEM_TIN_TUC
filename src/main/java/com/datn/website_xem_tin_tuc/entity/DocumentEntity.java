package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "documents")
@Data
public class DocumentEntity extends BaseEntity<Long>{
    @ManyToOne
    @JoinColumn(name = "articles_id")
    private ArticlesEntity articles;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String  description;

    @Column(name = "document_url")
    private String document_url;
}
