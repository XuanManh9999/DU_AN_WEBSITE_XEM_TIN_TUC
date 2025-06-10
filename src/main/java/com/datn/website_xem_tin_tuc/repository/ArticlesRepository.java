package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository  extends JpaRepository<ArticlesEntity, Long> {
}
