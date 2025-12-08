package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.CartItemEntity;
import com.datn.website_xem_tin_tuc.entity.CartEntity;
import com.datn.website_xem_tin_tuc.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByCartAndProduct(CartEntity cart, ProductEntity product);
    void deleteByCart(CartEntity cart);
}

