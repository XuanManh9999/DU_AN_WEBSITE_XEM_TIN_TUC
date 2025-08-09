package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticlesRepository  extends JpaRepository<ArticlesEntity, Long> {
    List<ArticlesEntity> findTop3ByCategory_IdAndIdNotOrderByCreateAtDesc(Long categoryId, Long excludedArticleId);
    Page<ArticlesEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Optional<ArticlesEntity> findBySlug(String slug);
    boolean existsByTitleIgnoreCase(String title);
    boolean existsBySlugIgnoreCase(String slug);
    Page<ArticlesEntity> findAllByCategory_SlugIgnoreCase(String slug, Pageable pageable);
    List<ArticlesEntity> findTop4ByCategory_IdOrderByCreateAtDesc(Long categoryId);

    @Query("SELECT MONTH(a.createAt), COUNT(a.id) " +
            "FROM ArticlesEntity a " +
            "WHERE YEAR(a.createAt) = :year " +
            "GROUP BY MONTH(a.createAt) " +
            "ORDER BY MONTH(a.createAt)")
    List<Object[]> countArticlesByMonth(@Param("year") int year);



    @Query("SELECT a FROM ArticlesEntity a " +
            "WHERE YEAR(a.createAt) = :year AND MONTH(a.createAt) = :month")
    Page<ArticlesEntity> findByMonthAndYear(@Param("year") int year,
                                            @Param("month") int month,
                                            Pageable pageable);


    @Query("SELECT a FROM ArticlesEntity a " +
            "WHERE YEAR(a.createAt) = :year " +
            "AND MONTH(a.createAt) = :month " +
            "AND (:day IS NULL OR DAY(a.createAt) = :day) " +
            "AND (:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ArticlesEntity> findByDateAndKeyword(
            @Param("year") int year,
            @Param("month") int month,
            @Param("day") Integer day,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}
