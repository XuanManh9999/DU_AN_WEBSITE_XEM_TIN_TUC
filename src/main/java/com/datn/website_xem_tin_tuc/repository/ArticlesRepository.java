package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticlesRepository  extends JpaRepository<ArticlesEntity, Long> {
    Page<ArticlesEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Optional<ArticlesEntity> findBySlug(String slug);
    boolean existsByTitleIgnoreCase(String title);
    boolean existsBySlugIgnoreCase(String slug);
}
