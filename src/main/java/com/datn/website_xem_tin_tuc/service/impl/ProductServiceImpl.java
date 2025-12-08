package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.request.ProductRequest;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.dto.response.ProductResponseDTO;
import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import com.datn.website_xem_tin_tuc.entity.ProductEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.CategoryRepository;
import com.datn.website_xem_tin_tuc.repository.ProductRepository;
import com.datn.website_xem_tin_tuc.service.CloudinaryService;
import com.datn.website_xem_tin_tuc.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public CommonResponse getAllProducts(int limit, int offset, String keyword, Long categoryId) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        Page<ProductEntity> page;

        if (keyword != null && !keyword.trim().isEmpty() || categoryId != null) {
            page = productRepository.searchProducts(keyword, categoryId, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        List<ProductResponseDTO> products = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return CommonResponse.builder()
                .status(200)
                .message("Lấy danh sách sản phẩm thành công")
                .data(products)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .currentPage(page.getNumber() + 1)
                .build();
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + id));
        return mapToDto(product);
    }

    @Override
    public ProductResponseDTO getProductBySlug(String slug) {
        ProductEntity product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với slug: " + slug));
        return mapToDto(product);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequest request, MultipartFile image) {
        if (productRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Slug sản phẩm đã tồn tại");
        }
        if (request.getSku() != null && productRepository.existsBySku(request.getSku())) {
            throw new DuplicateResourceException("SKU sản phẩm đã tồn tại");
        }

        ProductEntity product = new ProductEntity();
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setPriceSale(request.getPriceSale());
        product.setQuantity(request.getQuantity() != null ? request.getQuantity() : 0);
        product.setSize(request.getSize());
        product.setColor(request.getColor());
        product.setBrand(request.getBrand());
        product.setSku(request.getSku());
        product.setImages(request.getImages());

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFileToFolder(image, "products").getSecureUrl();
            product.setImage(imageUrl);
        } else if (request.getImage() != null) {
            product.setImage(request.getImage());
        }

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));
            product.setCategory(category);
        }

        ProductEntity saved = productRepository.save(product);
        return mapToDto(saved);
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequest request, MultipartFile image) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));

        if (request.getSlug() != null && !request.getSlug().equals(product.getSlug())) {
            if (productRepository.existsBySlug(request.getSlug())) {
                throw new DuplicateResourceException("Slug sản phẩm đã tồn tại");
            }
            product.setSlug(request.getSlug());
        }

        if (request.getName() != null) product.setName(request.getName());
        if (request.getDescription() != null) product.setDescription(request.getDescription());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getPriceSale() != null) product.setPriceSale(request.getPriceSale());
        if (request.getQuantity() != null) product.setQuantity(request.getQuantity());
        if (request.getSize() != null) product.setSize(request.getSize());
        if (request.getColor() != null) product.setColor(request.getColor());
        if (request.getBrand() != null) product.setBrand(request.getBrand());
        if (request.getSku() != null) {
            if (!request.getSku().equals(product.getSku()) && productRepository.existsBySku(request.getSku())) {
                throw new DuplicateResourceException("SKU sản phẩm đã tồn tại");
            }
            product.setSku(request.getSku());
        }
        if (request.getImages() != null) product.setImages(request.getImages());

        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadFileToFolder(image, "products").getSecureUrl();
            product.setImage(imageUrl);
        } else if (request.getImage() != null) {
            product.setImage(request.getImage());
        }

        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));
            product.setCategory(category);
        }

        ProductEntity updated = productRepository.save(product);
        return mapToDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm"));
        productRepository.delete(product);
    }

    private ProductResponseDTO mapToDto(ProductEntity entity) {
        return ProductResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .slug(entity.getSlug())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .priceSale(entity.getPriceSale())
                .image(entity.getImage())
                .images(entity.getImages())
                .quantity(entity.getQuantity())
                .size(entity.getSize())
                .color(entity.getColor())
                .brand(entity.getBrand())
                .sku(entity.getSku())
                .category(entity.getCategory() != null ? CategoryResponseDTO.builder()
                        .id(entity.getCategory().getId())
                        .name(entity.getCategory().getName())
                        .slug(entity.getCategory().getSlug())
                        .build() : null)
                .createAt(entity.getCreateAt())
                .updateAt(entity.getUpdateAt())
                .build();
    }
}

