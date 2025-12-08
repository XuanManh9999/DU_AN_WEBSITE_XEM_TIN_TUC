package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String orderCode;
    private UserResponseDTO user;
    private BigDecimal totalAmount;
    private BigDecimal shippingFee;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String shippingAddress;
    private String province;
    private String district;
    private String ward;
    private String note;
    private List<OrderItemResponseDTO> items;
    private Date createAt;
    private Date updateAt;
}

