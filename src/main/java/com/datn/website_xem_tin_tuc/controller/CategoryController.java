package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.service.CategoryService;
import com.google.api.Http;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .builder()
                        .status(HttpStatus.OK.value())
                        .message("Lấy tất cả thông tin danh mục thành công")
                        .data(categoryService.getAllAsTree(keyword))
                .build());
    }

    @GetMapping("/all-manage")
    public ResponseEntity<CommonResponse> getAllCategory(
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "") String keyword
    ) {
        CommonResponse response = categoryService.getAllCategoriesWithPagination(keyword, limit, offset);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/by-detail-articles")
    public ResponseEntity<?> getPostsByCategoryAndIgnoreCurrentPost (
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "articlesId") Long articlesId
    ) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(categoryService.getPostsByCategoryAndIgnoreCurrentPost(categoryId, articlesId));
    }

    @GetMapping("/by-articles")
    public ResponseEntity<?> getAllPostByCategory(
            @RequestParam(name = "slug") String slug,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "offset", defaultValue = "0") int offset
    ) {
        return ResponseEntity.status(HttpStatus.OK.value()).body(categoryService.getAllPostByCategory(slug, limit, offset));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh mục theo id thành công")
                .data(categoryService.getById(id))
                .build());
    }

    @PostMapping
    public ResponseEntity<?>  create(@RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("Thêm danh mục thành công")
                .data(categoryService.create(dto))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("Cập nhật danh mục thành công")
                .data(categoryService.update(id, dto))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message("Xóa danh mục thành công")
                .build());
    }
}
