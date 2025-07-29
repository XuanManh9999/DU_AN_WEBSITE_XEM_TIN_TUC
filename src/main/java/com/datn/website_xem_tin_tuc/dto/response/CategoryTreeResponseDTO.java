package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryTreeResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private Date createdAt;
    private Date updatedAt;

    private List<CategoryTreeResponseDTO> children = new ArrayList<>();

    private CategoryTreeResponseDTO parent; // 👈 Duy nhất 1 cha

    public CategoryTreeResponseDTO(Long id, String name, String slug, Date createAt, Date updateAt) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt =  createAt;
        this.updatedAt = updateAt;
    }

    public CategoryTreeResponseDTO(Long id, String name, String slug, Date createdAt, Date updatedAt, List<CategoryTreeResponseDTO> children, CategoryTreeResponseDTO parent) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.children = children;
        this.parent = parent;
    }
}