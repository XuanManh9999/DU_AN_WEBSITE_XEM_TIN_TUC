package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.CartItemRequest;
import com.datn.website_xem_tin_tuc.dto.response.CartResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

public interface CartService {
    CartResponseDTO getCart();
    CartResponseDTO addItemToCart(CartItemRequest request);
    CartResponseDTO updateCartItem(Long itemId, Integer quantity);
    void removeItemFromCart(Long itemId);
    void clearCart();
}

