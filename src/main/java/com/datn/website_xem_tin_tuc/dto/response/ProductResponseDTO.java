package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal priceSale;
    private String image;
    private String images;
    private Integer quantity;
    private String size;
    private String color;
    private String brand;
    private String sku;
    private CategoryResponseDTO category;
    private Date createAt;
    private Date updateAt;
}

