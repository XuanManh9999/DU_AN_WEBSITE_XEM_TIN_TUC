package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.TagArticlesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TagArticlesRepository extends JpaRepository<TagArticlesEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TagArticlesEntity t WHERE t.articles.id = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
}
