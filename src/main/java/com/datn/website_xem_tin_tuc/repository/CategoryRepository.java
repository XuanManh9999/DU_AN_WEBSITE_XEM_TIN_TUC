package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlugIgnoreCase(String slug);
    List<CategoryEntity> findByNameContainingIgnoreCase(String keyword);
    List<CategoryEntity> findByParentId(Long parentId);
}
