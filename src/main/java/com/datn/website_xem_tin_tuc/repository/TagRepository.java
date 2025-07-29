package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.TagEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    boolean existsByName(String name);
    Page<TagEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
