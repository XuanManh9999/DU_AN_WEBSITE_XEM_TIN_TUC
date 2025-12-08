package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.ProductRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.ProductResponseDTO;
import com.datn.website_xem_tin_tuc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<CommonResponse> getAllProducts(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseEntity.ok(productService.getAllProducts(limit, offset, keyword, categoryId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse> getProductById(@PathVariable Long id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin sản phẩm thành công")
                .data(product)
                .build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CommonResponse> getProductBySlug(@PathVariable String slug) {
        ProductResponseDTO product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy thông tin sản phẩm thành công")
                .data(product)
                .build());
    }

    @PostMapping
    public ResponseEntity<CommonResponse> createProduct(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String priceSale,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String images,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Long categoryId
    ) {
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .price(price != null ? new java.math.BigDecimal(price) : null)
                .priceSale(priceSale != null ? new java.math.BigDecimal(priceSale) : null)
                .images(images)
                .quantity(quantity)
                .size(size)
                .color(color)
                .brand(brand)
                .sku(sku)
                .categoryId(categoryId)
                .build();

        ProductResponseDTO product = productService.createProduct(request, image);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Thêm sản phẩm thành công")
                        .data(product)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> updateProduct(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String slug,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String priceSale,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String images,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Long categoryId
    ) {
        ProductRequest request = ProductRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .price(price != null ? new java.math.BigDecimal(price) : null)
                .priceSale(priceSale != null ? new java.math.BigDecimal(priceSale) : null)
                .images(images)
                .quantity(quantity)
                .size(size)
                .color(color)
                .brand(brand)
                .sku(sku)
                .categoryId(categoryId)
                .build();

        ProductResponseDTO product = productService.updateProduct(id, request, image);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật sản phẩm thành công")
                .data(product)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa sản phẩm thành công")
                .build());
    }
}

