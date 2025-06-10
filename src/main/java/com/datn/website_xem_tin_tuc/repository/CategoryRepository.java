package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Page<CategoryEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
