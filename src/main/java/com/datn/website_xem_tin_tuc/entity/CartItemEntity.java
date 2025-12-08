package com.datn.website_xem_tin_tuc.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItemEntity extends BaseEntity<Long> {
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "size")
    private String size;

    @Column(name = "color")
    private String color;
}

