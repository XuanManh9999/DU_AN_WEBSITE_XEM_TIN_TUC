package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private Integer quantity;
    private String size;
    private String color;
}

