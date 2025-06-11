package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CategoryTreeResponseDTO;
import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.CategoryRepository;
import com.datn.website_xem_tin_tuc.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryTreeResponseDTO> getAllAsTree(String keyword) {
        List<CategoryEntity> entities;

        if (keyword != null && !keyword.trim().isEmpty()) {
            entities = categoryRepository.findByNameContainingIgnoreCase(keyword);
        } else {
            entities = categoryRepository.findAll();
        }

        Map<Long, CategoryTreeResponseDTO> dtoMap = new HashMap<>();
        List<CategoryTreeResponseDTO> roots = new ArrayList<>();

        // B1: Map tất cả entity -> DTO
        for (CategoryEntity entity : entities) {
            CategoryTreeResponseDTO dto = new CategoryTreeResponseDTO(
                    entity.getId(),
                    entity.getName(),
                    entity.getSlug()
            );
            dtoMap.put(dto.getId(), dto);
        }

        // B2: Xây dựng cây
        for (CategoryEntity entity : entities) {
            CategoryTreeResponseDTO dto = dtoMap.get(entity.getId());
            if (entity.getParent() != null) {
                CategoryTreeResponseDTO parentDto = dtoMap.get(entity.getParent().getId());
                if (parentDto != null) {
                    parentDto.getChildren().add(dto);
                }
            } else {
                roots.add(dto);
            }
        }

        return roots;
    }


    @Override
    public CategoryTreeResponseDTO getById(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));

        // Tạo DTO từ danh mục gốc
        CategoryTreeResponseDTO rootDto = new CategoryTreeResponseDTO(
                category.getId(),
                category.getName(),
                category.getSlug()
        );

        // Tìm tất cả con trực tiếp (nếu cần đệ quy sâu hơn thì cần thay đổi logic)
        List<CategoryEntity> children = categoryRepository.findByParentId(category.getId());

        List<CategoryTreeResponseDTO> childDtos = children.stream()
                .map(child -> new CategoryTreeResponseDTO(
                        child.getId(),
                        child.getName(),
                        child.getSlug()
                ))
                .collect(Collectors.toList());

        rootDto.setChildren(childDtos);

        return rootDto;
    }

    @Override
    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("Tên danh mục đã tồn tại");
        }
        if (categoryRepository.existsBySlugIgnoreCase(dto.getSlug())) {
            throw new DuplicateResourceException("Slug danh mục đã tồn tại");
        }

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

        // Kiểm tra name trùng nếu đổi
        if (!category.getName().equalsIgnoreCase(dto.getName()) &&
                categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new DuplicateResourceException("Tên danh mục đã tồn tại");
        }

        // Kiểm tra slug trùng nếu đổi
        if (!category.getSlug().equalsIgnoreCase(dto.getSlug()) &&
                categoryRepository.existsBySlugIgnoreCase(dto.getSlug())) {
            throw new DuplicateResourceException("Slug danh mục đã tồn tại");
        }

        category.setName(dto.getName());
        category.setSlug(dto.getSlug());

        if (dto.getParentId() != null) {
            if (dto.getParentId().equals(id)) {
                throw new DuplicateResourceException("Danh mục không thể là cha của chính nó");
            }

            CategoryEntity parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new NotFoundException("Danh mục cha không tồn tại"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        CategoryEntity updated = categoryRepository.save(category);
        return modelMapper.map(updated, CategoryResponseDTO.class);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        categoryRepository.delete(category);
    }
}
