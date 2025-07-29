package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.dto.request.CategoryRequestDTO;
import com.datn.website_xem_tin_tuc.dto.response.*;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.CategoryEntity;
import com.datn.website_xem_tin_tuc.entity.TagArticlesEntity;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.BookmarkRepository;
import com.datn.website_xem_tin_tuc.repository.CategoryRepository;
import com.datn.website_xem_tin_tuc.repository.LikeRepository;
import com.datn.website_xem_tin_tuc.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ArticlesRepository articlesRepository;

    @Override
    public CommonResponse getPostsByCategoryAndIgnoreCurrentPost(Long categoryId, Long articlesId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        articlesRepository.findById(articlesId).orElseThrow(() -> new NotFoundException("Bài viết không tồn tại"));

        List<ArticlesEntity> articlesEntities = articlesRepository.findTop3ByCategory_IdAndIdNotOrderByCreateAtDesc(categoryId, articlesId);
        List<ArticlesResponseDTO> articlesResponseDTOS = new ArrayList<>();
        for (ArticlesEntity articlesEntity : articlesEntities) {
            articlesResponseDTOS.add(mapToDto(articlesEntity));
        }
        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy bài viết liên quan thành công")
                .data(articlesResponseDTOS)
                .build();

    }

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
                    entity.getSlug(),
                    entity.getCreateAt(),
                    entity.getUpdateAt()
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

        CategoryTreeResponseDTO dto = buildCategoryTreeWithChildren(category);

        // Nếu có cha thì set vào DTO
        if (category.getParent() != null) {
            CategoryEntity parentEntity = category.getParent();
            CategoryTreeResponseDTO parentDto = new CategoryTreeResponseDTO(
                    parentEntity.getId(),
                    parentEntity.getName(),
                    parentEntity.getSlug(),
                    parentEntity.getCreateAt(),
                    parentEntity.getUpdateAt()
            );
            dto.setParent(parentDto);
        }

        return dto;
    }



    private CategoryTreeResponseDTO buildCategoryTree(CategoryEntity category) {
        // Tạo DTO hiện tại
        CategoryTreeResponseDTO dto = new CategoryTreeResponseDTO(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getCreateAt(),
                category.getUpdateAt()
        );

        // Lấy children đệ quy
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryTreeResponseDTO> childDtos = category.getChildren().stream()
                    .map(this::buildCategoryTree)
                    .collect(Collectors.toList());
            dto.setChildren(childDtos);
        }

        return dto;
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
    public CommonResponse getAllPostByCategory(String slug, int limit, int offset) {
        CategoryEntity category = categoryRepository.findBySlugIgnoreCase(slug)
                .orElseThrow(() -> new NotFoundException("Category không tồn tại"));

        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createAt"));

        Page<ArticlesEntity> articlesPage = articlesRepository
                .findAllByCategory_SlugIgnoreCase(slug, pageable);

        List<ArticlesResponseDTO> articlesResponseDTOS = articlesPage.getContent()
                .stream()
                .map(this::mapToDto)
                .toList();

        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy tất cả bài viết thành công")
                .data(articlesResponseDTOS)
                .totalItems(articlesPage.getTotalElements())
                .totalPages(articlesPage.getTotalPages())
                .currentPage(articlesPage.getNumber())
                .build();
    }



    @Override
    @Transactional
    public void delete(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Danh mục không tồn tại"));
        categoryRepository.delete(category);
    }

    @Override
    public CommonResponse getAllCategoriesWithPagination(String keyword, int limit, int offset) {
        Pageable pageable = PageRequest.of(offset, limit, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<CategoryEntity> categoryPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            categoryPage = categoryRepository.findAll(pageable);
        } else {
            categoryPage = categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        }
        List<CategoryResponseDTO> categoryDTOs = categoryPage.getContent().stream()
                .map(category -> {
                    CategoryResponseDTO categoryResponseDTO = modelMapper.map(category, CategoryResponseDTO.class);
                    categoryResponseDTO.setCreateAt(category.getCreateAt());
                    categoryResponseDTO.setUpdateAt(category.getUpdateAt());
                    return categoryResponseDTO;
                })
                .toList();

        return CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Lấy danh mục thành công")
                .data(categoryDTOs)
                .totalItems(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .currentPage(categoryPage.getNumber())
                .build();
    }


    private ArticlesResponseDTO mapToDto(ArticlesEntity entity) {
        Integer quantityLike = likeRepository.countByArticles(entity);
        Integer quantityBookmark = bookmarkRepository.countByArticles(entity);

        List<TagResponse> tagResponses = new ArrayList<>();


        for (TagArticlesEntity tagArticlesEntity : entity.getTagArticlesEntities()) {
            tagResponses.add(TagResponse.builder()
                    .id(tagArticlesEntity.getTag().getId())
                    .name(tagArticlesEntity.getTag().getName())
                    .description(tagArticlesEntity.getTag().getDescription())
                    .createAt(tagArticlesEntity.getTag().getCreateAt())
                    .updateAt(tagArticlesEntity.getTag().getUpdateAt())
                    .build());
        }

        return ArticlesResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .slug(entity.getSlug())
                .thumbnail(entity.getThumbnail())
                .view(entity.getView())
                .active(entity.getActive())
                .quantityBookmark(quantityBookmark)
                .quantityLike(quantityLike)
                .slugCategory(entity.getCategory().getSlug())
                .author(UserResponseDTO.builder()
                        .id(entity.getAuthor().getId())
                        .username(entity.getAuthor().getUsername())
                        .phoneNumber(entity.getAuthor().getPhoneNumber())
                        .email(entity.getAuthor().getEmail())
                        .background(entity.getAuthor().getBackground())
                        .avatar(entity.getAuthor().getAvatar())
                        .createdAt(entity.getAuthor().getCreateAt())
                        .updatedAt(entity.getAuthor().getUpdateAt())
                        .build())
                .category(CategoryResponseDTO.builder()
                        .name(entity.getCategory().getName())
                        .id(entity.getCategory().getId())
                        .slug(entity.getCategory().getSlug())
                        .build())
                .tags(tagResponses)
                .createAt(entity.getCreateAt())
                .build();
    }


    private ArticlesResponseDTO mapToDtoTop3(ArticlesEntity entity) {
        List<TagResponse> tagResponses = new ArrayList<>();

        for (TagArticlesEntity tagArticlesEntity : entity.getTagArticlesEntities()) {
            tagResponses.add(TagResponse.builder()
                    .id(tagArticlesEntity.getTag().getId())
                    .name(tagArticlesEntity.getTag().getName())
                    .description(tagArticlesEntity.getTag().getDescription())
                    .createAt(tagArticlesEntity.getTag().getCreateAt())
                    .updateAt(tagArticlesEntity.getTag().getUpdateAt())
                    .build());
        }

        return ArticlesResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .slug(entity.getSlug())
                .thumbnail(entity.getThumbnail())
                .view(entity.getView())
                .active(entity.getActive())
                .category(CategoryResponseDTO.builder()
                        .name(entity.getCategory().getName())
                        .id(entity.getCategory().getId())
                        .slug(entity.getCategory().getSlug())
                        .build())
                .tags(tagResponses)
                .createAt(entity.getCreateAt())
                .build();
    }


    private CategoryTreeResponseDTO buildCategoryTreeWithChildren(CategoryEntity category) {
        CategoryTreeResponseDTO dto = new CategoryTreeResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setCreatedAt(category.getCreateAt());
        dto.setUpdatedAt(category.getUpdateAt());

        // Xử lý children (nhiều con)
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryTreeResponseDTO> childDtos = category.getChildren().stream()
                    .map(this::buildCategoryTreeWithChildren)
                    .collect(Collectors.toList());
            dto.setChildren(childDtos);
        }

        return dto;
    }


}
