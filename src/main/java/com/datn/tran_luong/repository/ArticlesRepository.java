package com.datn.tran_luong.repository;

import com.datn.tran_luong.entity.ArticlesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlesRepository  extends JpaRepository<ArticlesEntity, Long> {
}
