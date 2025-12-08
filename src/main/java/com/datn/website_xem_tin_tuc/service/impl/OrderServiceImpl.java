package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.CartItemRequest;
import com.datn.website_xem_tin_tuc.dto.request.OrderRequest;
import com.datn.website_xem_tin_tuc.dto.response.*;
import com.datn.website_xem_tin_tuc.entity.*;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.*;
import com.datn.website_xem_tin_tuc.service.CartService;
import com.datn.website_xem_tin_tuc.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final CurrentUser currentUser;

    private String generateOrderCode() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "ORD" + timestamp + random;
    }

    @Override
    public CommonResponse getAllOrders(int limit, int offset, String keyword, String status) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<OrderEntity> page = orderRepository.searchOrders(keyword, status, pageable);

        List<OrderResponseDTO> orders = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return CommonResponse.builder()
                .status(200)
                .message("Lấy danh sách đơn hàng thành công")
                .data(orders)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .build();
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
        return mapToDto(order);
    }

    @Override
    public OrderResponseDTO getOrderByCode(String orderCode) {
        OrderEntity order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với mã: " + orderCode));
        return mapToDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequest request) {
        UserEntity user = currentUser.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Người dùng chưa đăng nhập");
        }

        OrderEntity order = new OrderEntity();
        order.setOrderCode(generateOrderCode());
        order.setUser(user);
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setShippingAddress(request.getShippingAddress());
        order.setProvince(request.getProvince());
        order.setDistrict(request.getDistrict());
        order.setWard(request.getWard());
        order.setPaymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "COD");
        order.setPaymentStatus("UNPAID");
        order.setStatus("PENDING");
        order.setNote(request.getNote());
        order.setShippingFee(request.getShippingFee() != null ? request.getShippingFee() : BigDecimal.ZERO);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Tạo order items từ request
        for (CartItemRequest itemRequest : request.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm: " + itemRequest.getProductId()));

            if (product.getQuantity() < itemRequest.getQuantity()) {
                throw new IllegalArgumentException("Số lượng sản phẩm " + product.getName() + " không đủ");
            }

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSize(itemRequest.getSize());
            orderItem.setColor(itemRequest.getColor());
            
            BigDecimal itemPrice = product.getPriceSale() != null ? product.getPriceSale() : product.getPrice();
            orderItem.setPrice(itemPrice);
            
            totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            // Giảm số lượng sản phẩm
            product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount.add(order.getShippingFee()));
        OrderEntity savedOrder = orderRepository.save(order);

        // Lưu order items
        for (CartItemRequest itemRequest : request.getItems()) {
            ProductEntity product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setSize(itemRequest.getSize());
            orderItem.setColor(itemRequest.getColor());
            
            BigDecimal itemPrice = product.getPriceSale() != null ? product.getPriceSale() : product.getPrice();
            orderItem.setPrice(itemPrice);
            
            orderItemRepository.save(orderItem);
        }

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cartService.clearCart();

        return mapToDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        OrderEntity updated = orderRepository.save(order);
        return mapToDto(updated);
    }

    @Override
    public CommonResponse getUserOrders(int limit, int offset, String keyword, String status) {
        UserEntity user = currentUser.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Người dùng chưa đăng nhập");
        }

        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<OrderEntity> page = orderRepository.searchUserOrders(user, keyword, status, pageable);

        List<OrderResponseDTO> orders = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return CommonResponse.builder()
                .status(200)
                .message("Lấy danh sách đơn hàng thành công")
                .data(orders)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .build();
    }

    private OrderResponseDTO mapToDto(OrderEntity entity) {
        List<OrderItemEntity> items = entity.getOrderItems() != null ? entity.getOrderItems() : List.of();
        
        List<OrderItemResponseDTO> itemDtos = items.stream()
                .map(this::mapItemToDto)
                .collect(Collectors.toList());

        UserResponseDTO userDto = null;
        if (entity.getUser() != null) {
            userDto = UserResponseDTO.builder()
                    .id(entity.getUser().getId())
                    .username(entity.getUser().getUsername())
                    .email(entity.getUser().getEmail())
                    .phoneNumber(entity.getUser().getPhoneNumber())
                    .build();
        }

        return OrderResponseDTO.builder()
                .id(entity.getId())
                .orderCode(entity.getOrderCode())
                .user(userDto)
                .totalAmount(entity.getTotalAmount())
                .shippingFee(entity.getShippingFee())
                .status(entity.getStatus())
                .paymentMethod(entity.getPaymentMethod())
                .paymentStatus(entity.getPaymentStatus())
                .customerName(entity.getCustomerName())
                .customerPhone(entity.getCustomerPhone())
                .customerEmail(entity.getCustomerEmail())
                .shippingAddress(entity.getShippingAddress())
                .province(entity.getProvince())
                .district(entity.getDistrict())
                .ward(entity.getWard())
                .note(entity.getNote())
                .items(itemDtos)
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }

    private OrderItemResponseDTO mapItemToDto(OrderItemEntity item) {
        ProductEntity product = item.getProduct();
        ProductResponseDTO productDto = ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .image(product.getImage())
                .price(product.getPrice())
                .priceSale(product.getPriceSale())
                .build();

        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .product(productDto)
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .size(item.getSize())
                .color(item.getColor())
                .build();
    }
}

