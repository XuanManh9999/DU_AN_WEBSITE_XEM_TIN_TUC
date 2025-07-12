package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    Optional<TagEntity> findByName(String name);
    boolean existsByName(String name);
}
