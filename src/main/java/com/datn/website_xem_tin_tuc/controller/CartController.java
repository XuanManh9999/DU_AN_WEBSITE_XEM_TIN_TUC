package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.CartItemRequest;
import com.datn.website_xem_tin_tuc.dto.response.CartResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CommonResponse> getCart() {
        CartResponseDTO cart = cartService.getCart();
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy giỏ hàng thành công")
                .data(cart)
                .build());
    }

    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addItemToCart(@RequestBody CartItemRequest request) {
        CartResponseDTO cart = cartService.addItemToCart(request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Thêm sản phẩm vào giỏ hàng thành công")
                .data(cart)
                .build());
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CommonResponse> updateCartItem(
            @PathVariable Long itemId,
            @RequestParam Integer quantity
    ) {
        CartResponseDTO cart = cartService.updateCartItem(itemId, quantity);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật giỏ hàng thành công")
                .data(cart)
                .build());
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CommonResponse> removeItemFromCart(@PathVariable Long itemId) {
        cartService.removeItemFromCart(itemId);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa sản phẩm khỏi giỏ hàng thành công")
                .build());
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CommonResponse> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa giỏ hàng thành công")
                .build());
    }
}

