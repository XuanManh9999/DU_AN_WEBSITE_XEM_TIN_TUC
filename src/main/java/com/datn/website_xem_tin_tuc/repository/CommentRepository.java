package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByArticlesIdAndParentIsNullOrderByCreateAtDesc(Long articleId);
}
