package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.BookmarkEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    Optional<BookmarkEntity> findByUserAndArticles(UserEntity user, ArticlesEntity articles);
//    List<BookmarkEntity> findAllByUser(UserEntity user);
    Page<BookmarkEntity> findAllByUser(UserEntity user, Pageable pageable);
    boolean existsByUserAndArticles(UserEntity user, ArticlesEntity articles);
    Integer countByArticles(ArticlesEntity articles);
}
