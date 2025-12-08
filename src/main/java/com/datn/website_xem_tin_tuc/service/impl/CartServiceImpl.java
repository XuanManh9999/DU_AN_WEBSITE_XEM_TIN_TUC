package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.CartItemRequest;
import com.datn.website_xem_tin_tuc.dto.response.CartItemResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CartResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.ProductResponseDTO;
import com.datn.website_xem_tin_tuc.entity.CartEntity;
import com.datn.website_xem_tin_tuc.entity.CartItemEntity;
import com.datn.website_xem_tin_tuc.entity.ProductEntity;
import com.datn.website_xem_tin_tuc.entity.UserEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.CartItemRepository;
import com.datn.website_xem_tin_tuc.repository.CartRepository;
import com.datn.website_xem_tin_tuc.repository.ProductRepository;
import com.datn.website_xem_tin_tuc.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CurrentUser currentUser;

    private CartEntity getOrCreateCart() {
        UserEntity user = currentUser.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Người dùng chưa đăng nhập");
        }

        Optional<CartEntity> cartOpt = cartRepository.findByUser(user);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        }

        CartEntity cart = new CartEntity();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public CartResponseDTO getCart() {
        CartEntity cart = getOrCreateCart();
        return mapToDto(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addItemToCart(CartItemRequest request) {
        CartEntity cart = getOrCreateCart();
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        }

        Optional<CartItemEntity> existingItemOpt = cartItemRepository.findByCartAndProduct(cart, product);
        
        if (existingItemOpt.isPresent()) {
            CartItemEntity existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getQuantity() < newQuantity) {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
            }
            existingItem.setQuantity(newQuantity);
            if (request.getSize() != null) existingItem.setSize(request.getSize());
            if (request.getColor() != null) existingItem.setColor(request.getColor());
            cartItemRepository.save(existingItem);
        } else {
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setSize(request.getSize());
            cartItem.setColor(request.getColor());
            cartItemRepository.save(cartItem);
        }

        return mapToDto(cartRepository.findById(cart.getId()).orElse(cart));
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItem(Long itemId, Integer quantity) {
        CartItemEntity cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
        } else {
            if (cartItem.getProduct().getQuantity() < quantity) {
                throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }

        CartEntity cart = cartItem.getCart();
        return mapToDto(cartRepository.findById(cart.getId()).orElse(cart));
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long itemId) {
        CartItemEntity cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm trong giỏ hàng"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart() {
        CartEntity cart = getOrCreateCart();
        cartItemRepository.deleteByCart(cart);
    }

    private CartResponseDTO mapToDto(CartEntity cart) {
        // Reload cart with items
        CartEntity reloadedCart = cartRepository.findById(cart.getId())
                .orElse(cart);
        List<CartItemEntity> items = reloadedCart.getCartItems() != null ? reloadedCart.getCartItems() : List.of();
        
        List<CartItemResponseDTO> itemDtos = items.stream()
                .map(this::mapItemToDto)
                .collect(Collectors.toList());

        int totalItems = items.stream()
                .mapToInt(CartItemEntity::getQuantity)
                .sum();

        BigDecimal totalAmount = items.stream()
                .map(item -> {
                    BigDecimal price = item.getProduct().getPriceSale() != null 
                            ? item.getProduct().getPriceSale() 
                            : item.getProduct().getPrice();
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponseDTO.builder()
                .id(cart.getId())
                .items(itemDtos)
                .totalItems(totalItems)
                .totalAmount(totalAmount)
                .build();
    }

    private CartItemResponseDTO mapItemToDto(CartItemEntity item) {
        ProductEntity product = item.getProduct();
        ProductResponseDTO productDto = ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .image(product.getImage())
                .price(product.getPrice())
                .priceSale(product.getPriceSale())
                .quantity(product.getQuantity())
                .build();

        return CartItemResponseDTO.builder()
                .id(item.getId())
                .product(productDto)
                .quantity(item.getQuantity())
                .size(item.getSize())
                .color(item.getColor())
                .build();
    }
}

