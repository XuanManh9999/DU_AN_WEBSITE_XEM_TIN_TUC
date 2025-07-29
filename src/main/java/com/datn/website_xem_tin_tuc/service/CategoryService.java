package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryTreeResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import java.util.List;

public interface CategoryService {
    CommonResponse getPostsByCategoryAndIgnoreCurrentPost(Long categoryId, Long articlesId);
    List<CategoryTreeResponseDTO> getAllAsTree(String keyword);
    CategoryTreeResponseDTO getById(Long id);
    CategoryResponseDTO create(CategoryRequestDTO dto);
    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);
    CommonResponse getAllPostByCategory(String slug, int limit, int offset);
    void delete(Long id);
    CommonResponse getAllCategoriesWithPagination(String keyword, int limit, int offset);
}
