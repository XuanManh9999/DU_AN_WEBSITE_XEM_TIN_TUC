package com.datn.website_xem_tin_tuc.dto.response;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private Date createAt;
    private Date updateAt;
}