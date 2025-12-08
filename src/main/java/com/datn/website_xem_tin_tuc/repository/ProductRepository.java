package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);
    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    @Query("SELECT p FROM ProductEntity p WHERE " +
           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<ProductEntity> searchProducts(@Param("keyword") String keyword, 
                                       @Param("categoryId") Long categoryId, 
                                       Pageable pageable);
}

