package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTreeResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private List<CategoryTreeResponseDTO> children = new ArrayList<>();

    public CategoryTreeResponseDTO(Long id, String name, String slug) {
        this.id = id;
        this.name = name;
        this.slug = slug;
    }
}