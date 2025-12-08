package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.OrderRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.OrderResponseDTO;
import com.datn.website_xem_tin_tuc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllOrders(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(limit, offset, keyword, status));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<CommonResponse> getUserOrders(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        return ResponseEntity.ok(orderService.getUserOrders(limit, offset, keyword, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getOrderById(@PathVariable Long id) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin đơn hàng thành công")
                .data(order)
                .build());
    }

    @GetMapping("/code/{orderCode}")
    public ResponseEntity<CommonResponse> getOrderByCode(@PathVariable String orderCode) {
        OrderResponseDTO order = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin đơn hàng thành công")
                .data(order)
                .build());
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponseDTO order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Đặt hàng thành công")
                        .data(order)
                        .build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CommonResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        OrderResponseDTO order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật trạng thái đơn hàng thành công")
                .data(order)
                .build());
    }
}

