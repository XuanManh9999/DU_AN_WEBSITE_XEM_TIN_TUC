package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.OrderEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderCode(String orderCode);
    Page<OrderEntity> findByUser(UserEntity user, Pageable pageable);
    
    @Query("SELECT o FROM OrderEntity o WHERE " +
           "(:keyword IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.customerPhone) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<OrderEntity> searchOrders(@Param("keyword") String keyword, 
                                   @Param("status") String status, 
                                   Pageable pageable);
    
    @Query("SELECT o FROM OrderEntity o WHERE o.user = :user AND " +
           "(:keyword IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.customerName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(o.customerPhone) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR o.status = :status)")
    Page<OrderEntity> searchUserOrders(@Param("user") UserEntity user,
                                       @Param("keyword") String keyword, 
                                       @Param("status") String status, 
                                       Pageable pageable);
}

