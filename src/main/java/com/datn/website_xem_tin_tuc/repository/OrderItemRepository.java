package com.datn.website_xem_tin_tuc.repository;

import com.datn.website_xem_tin_tuc.entity.OrderItemEntity;
import com.datn.website_xem_tin_tuc.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrder(OrderEntity order);
}

