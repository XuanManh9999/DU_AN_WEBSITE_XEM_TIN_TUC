package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.ProductRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    CommonResponse getAllProducts(int limit, int offset, String keyword, Long categoryId);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO getProductBySlug(String slug);
    ProductResponseDTO createProduct(ProductRequest request, MultipartFile image);
    ProductResponseDTO updateProduct(Long id, ProductRequest request, MultipartFile image);
    void deleteProduct(Long id);
}

