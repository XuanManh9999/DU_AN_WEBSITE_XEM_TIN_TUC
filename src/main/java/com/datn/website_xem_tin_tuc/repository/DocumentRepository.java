package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
}
