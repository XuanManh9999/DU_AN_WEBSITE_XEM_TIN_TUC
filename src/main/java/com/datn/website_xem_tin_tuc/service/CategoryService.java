package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryTreeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryTreeResponseDTO> getAllAsTree(String keyword);
    CategoryTreeResponseDTO getById(Long id);
    CategoryResponseDTO create(CategoryRequestDTO dto);
    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);
    void delete(Long id);
}
