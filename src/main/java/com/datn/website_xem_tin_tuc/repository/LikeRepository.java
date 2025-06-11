package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.BookmarkEntity;
import com.datn.website_xem_tin_tuc.entity.LikeEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndArticles(UserEntity user, ArticlesEntity articles);
    List<LikeEntity> findAllByUser(UserEntity user);
    boolean existsByUserAndArticles(UserEntity user, ArticlesEntity articles);
    Integer countByArticles(ArticlesEntity articles);
}
