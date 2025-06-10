package com.datn.website_xem_tin_tuc.dto.response;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
}