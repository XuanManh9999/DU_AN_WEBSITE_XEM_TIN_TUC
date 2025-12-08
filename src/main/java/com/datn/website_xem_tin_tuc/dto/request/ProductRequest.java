package com.datn.website_xem_tin_tuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private BigDecimal price;
    private BigDecimal priceSale;
    private String image;
    private String images; // JSON array
    private Integer quantity;
    private String size;
    private String color;
    private String brand;
    private String sku;
    private Long categoryId;
}

