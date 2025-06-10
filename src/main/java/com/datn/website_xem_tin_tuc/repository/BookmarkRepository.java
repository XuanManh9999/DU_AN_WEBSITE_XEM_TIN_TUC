package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
}
