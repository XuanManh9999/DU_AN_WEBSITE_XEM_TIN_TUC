package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.OrderRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.OrderResponseDTO;

public interface OrderService {
    CommonResponse getAllOrders(int limit, int offset, String keyword, String status);
    OrderResponseDTO getOrderById(Long id);
    OrderResponseDTO getOrderByCode(String orderCode);
    OrderResponseDTO createOrder(OrderRequest request);
    OrderResponseDTO updateOrderStatus(Long id, String status);
    CommonResponse getUserOrders(int limit, int offset, String keyword, String status);
}

