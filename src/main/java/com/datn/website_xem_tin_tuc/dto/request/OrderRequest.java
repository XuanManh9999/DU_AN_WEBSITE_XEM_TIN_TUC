package com.datn.website_xem_tin_tuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String shippingAddress;
    private String province;
    private String district;
    private String ward;
    private String paymentMethod;
    private String note;
    private BigDecimal shippingFee;
    private List<CartItemRequest> items; // Items from cart or direct order
}

