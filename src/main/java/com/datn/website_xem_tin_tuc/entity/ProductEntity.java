package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
@Data
public class ProductEntity extends BaseEntity<Long> {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", unique = true, nullable = false)
    private String slug;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "price_sale", precision = 10, scale = 2)
    private BigDecimal priceSale;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images; // JSON array of image URLs

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "size")
    private String size; // S, M, L, XL, XXL

    @Column(name = "color")
    private String color; // Màu sắc

    @Column(name = "brand")
    private String brand; // Thương hiệu

    @Column(name = "sku", unique = true)
    private String sku; // Mã sản phẩm

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<CartItemEntity> cartItems;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItemEntity> orderItems;
}

