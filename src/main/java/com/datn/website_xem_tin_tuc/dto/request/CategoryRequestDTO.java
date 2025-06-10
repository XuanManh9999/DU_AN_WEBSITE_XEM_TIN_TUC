package com.datn.website_xem_tin_tuc.dto.request;

import lombok.Data;

@Data
public class CategoryRequestDTO {
    private String name;
    private String slug;
    private Long parentId; // có thể null nếu là category cha
}