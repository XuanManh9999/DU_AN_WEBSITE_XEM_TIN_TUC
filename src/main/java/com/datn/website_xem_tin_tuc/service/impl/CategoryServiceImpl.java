package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.CategoryRepository;
import com.datn.website_xem_tin_tuc.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<CategoryResponseDTO> getAll(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable)
                .map(category -> {
                    CategoryResponseDTO dto = modelMapper.map(category, CategoryResponseDTO.class);
                    dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
                    return dto;
                });
    }

    @Override
    public CategoryResponseDTO getById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        CategoryResponseDTO dto = modelMapper.map(category, CategoryResponseDTO.class);
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        return dto;
    }

    @Override
    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        CategoryEntity entity = modelMapper.map(dto, CategoryEntity.class);
        if (dto.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Danh mục cha không tồn tại"));
            entity.setParent(parent);
        }
        CategoryEntity saved = categoryRepository.save(entity);
        return modelMapper.map(saved, CategoryResponseDTO.class);
    }

    @Override
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));

        category.setName(dto.getName());
        category.setSlug(dto.getSlug());

        if (dto.getParentId() != null) {
            CategoryEntity parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Danh mục cha không tồn tại"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return modelMapper.map(categoryRepository.save(category), CategoryResponseDTO.class);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        categoryRepository.delete(category);
    }
}
