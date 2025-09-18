package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.request.ArticlesRequest;
import com.datn.website_xem_tin_tuc.dto.response.*;
import com.datn.website_xem_tin_tuc.entity.*;
import com.datn.website_xem_tin_tuc.enums.TypeArticles;
import com.datn.website_xem_tin_tuc.exceptions.customs.DuplicateResourceException;
import com.datn.website_xem_tin_tuc.exceptions.customs.NotFoundException;
import com.datn.website_xem_tin_tuc.repository.*;
import com.datn.website_xem_tin_tuc.service.ArticlesService;
import com.datn.website_xem_tin_tuc.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticlesServiceImpl implements ArticlesService {
    private final ArticlesRepository articlesRepository;
    private final CloudinaryService cloudinaryService;
    private final TagArticlesRepository tagArticlesRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CurrentUser currentUser;
//    private final StringRedisTemplate redisTemplate;
    private final TagRepository tagRepository;
//    @Override
//    public Map<Long, Long> fetchAndResetViewCounts() {
//        Set<String> keys = redisTemplate.keys("article:view:*");
//        Map<Long, Long> result = new HashMap<>();
//
//        if (keys != null) {
//            for (String key : keys) {
//                String articleIdStr = key.split(":")[2];
//                Long articleId = Long.parseLong(articleIdStr);
//
//                String value = redisTemplate.opsForValue().get(key);
//                if (value != null) {
//                    Long views = Long.parseLong(value);
//                    result.put(articleId, views);
//
//                    // Xoá key sau khi lấy
//                    redisTemplate.delete(key);
//                }
//            }
//        }
//        return result;
//    }
    // dùng redis
//    @Override
//    public void increaseView(Long articleId, String userKey) {
//        String viewCacheKey = "article:viewed:" + articleId + ":" + userKey;
//        Boolean alreadyViewed = redisTemplate.hasKey(viewCacheKey);
//
//        if (!alreadyViewed) {
//            // Tăng view count
//            redisTemplate.opsForValue().increment("article:view:" + articleId);
//
//            // Đánh dấu đã xem để chống spam
//            redisTemplate.opsForValue().set(viewCacheKey, "1", Duration.ofHours(6));
//        }
//    }
//    Không dùng thêm view tự động

    @Override
    public void increaseView(Long articleId) {
        ArticlesEntity articles = articlesRepository.findById(articleId).orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));
        Integer newView = articles.getView() + 1;
        articles.setView(newView);
        articlesRepository.save(articles);
    }


    @Override
    public CommonResponse getAllArticles(int limit, int offset, String sortBy, String order, String title) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(direction, sortBy));

        Page<ArticlesEntity> page;

        if (title != null && !title.trim().isEmpty()) {
            page = articlesRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            page = articlesRepository.findAll(pageable);
        }

        List<ArticlesResponseDTO> articles = page.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return CommonResponse.builder()
                .status(200)
                .message("Lấy danh sách bài viết thành công")
                .data(articles)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .currentPage(page.getNumber() + 1) // page.getNumber() là index bắt đầu từ 0
                .build();
    }


    @Override
    public ArticlesResponseDTO getArticleById(Long id) {
        ArticlesEntity article = articlesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết dựa trên ID cung cấp: " + id));
        return mapToDto(article);
    }
    @Override
    public ArticlesResponseDTO createArticle(String title, String slug, String content,
                                             MultipartFile thumbnail, TypeArticles typeEnum,
                                             List<Long> tagIds, Long categoryId) {
        if (articlesRepository.existsByTitleIgnoreCase(title)) {
            throw new DuplicateResourceException("Tiêu đề bài viết đã tồn tại");
        }

        if (articlesRepository.existsBySlugIgnoreCase(slug)) {
            throw new DuplicateResourceException("Slug bài viết đã tồn tại");
        }

        ArticlesEntity article = new ArticlesEntity();
        article.setTitle(title);
        article.setSlug(slug);
        article.setContent(content);
        article.setType(typeEnum);
        article.setView(0);

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = cloudinaryService.uploadFileToFolder(thumbnail, "articles").getSecureUrl();
            article.setThumbnail(thumbnailUrl);
        }

        UserEntity user = currentUser.getCurrentUser();
        if (user == null) {
            throw new NotFoundException("Không xác định được người dùng hiện tại");
        }
        article.setAuthor(user);
        article.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục")));

        if (article.getTagArticlesEntities() == null) {
            article.setTagArticlesEntities(new ArrayList<>());
        }


        ArticlesEntity saved = articlesRepository.save(article);
        for (Long tagId : tagIds) {
            TagEntity tag = tagRepository.findById(Math.toIntExact(tagId))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy tag"));

            TagArticlesEntity tagArticle = new TagArticlesEntity();
            tagArticle.setArticles(article);  // gán bài viết
            tagArticle.setTag(tag);          // gán tag
            tagArticlesRepository.save(tagArticle);
            article.getTagArticlesEntities().add(tagArticle);
        }
        return mapToDto(saved);
    }


    @Override
    public ArticlesResponseDTO updateArticle(Long id, ArticlesRequest request, MultipartFile thumbnail) {
        ArticlesEntity article = articlesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));

        // Kiểm tra trùng title (nếu đổi title)
        if (request.getTitle() != null && !request.getTitle().equalsIgnoreCase(article.getTitle())) {
            if (articlesRepository.existsByTitleIgnoreCase(request.getTitle())) {
                throw new DuplicateResourceException("Tiêu đề bài viết đã tồn tại");
            }
            article.setTitle(request.getTitle());
        }

        // Kiểm tra trùng slug (nếu đổi slug)
        if (request.getSlug() != null && !request.getSlug().equalsIgnoreCase(article.getSlug())) {
            if (articlesRepository.existsBySlugIgnoreCase(request.getSlug())) {
                throw new DuplicateResourceException("Slug bài viết đã tồn tại");
            }
            article.setSlug(request.getSlug());
        }

        if (request.getContent() != null) {
            article.setContent(request.getContent());
        }

        if (request.getAuthorId() != null) {
            article.setAuthor(userRepository.findById(request.getAuthorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng")));
        }
        if (request.getType() != null) {
            article.setType(request.getType());
        }

        if (request.getCategoryId() != null) {
            article.setCategory(categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục")));
        }

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String thumbnailUrl = cloudinaryService.uploadFileToFolder(thumbnail, "articles").getSecureUrl();
            article.setThumbnail(thumbnailUrl);
        }

        ArticlesEntity updated = articlesRepository.save(article);
        return mapToDto(updated);
    }


    @Override
    public void deleteArticle(Long id) {
        ArticlesEntity article = articlesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết"));

        // Xóa tất cả bản ghi liên kết với article_id trong bảng trung gian
        tagArticlesRepository.deleteByArticleId(id);

        // Sau đó mới xoá article
        articlesRepository.delete(article);
    }

    @Override
    public ArticlesResponseDTO getArticleBySlug(String slug) {
        ArticlesEntity entity = articlesRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy bài viết với slug: " + slug));
        return mapToDto(entity);
    }

    @Override
    public List<ArticlesResponseDTO> getArticleByCategoryId(Long categoryId) {
        List<ArticlesEntity> articles = articlesRepository
                .findTop4ByCategory_IdOrderByCreateAtDesc(categoryId);

        return articles.stream()
                .map(this::mapToDto)
                .toList();
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
                .isLike(likeRepository.existsByUserAndArticles(currentUser.getCurrentUser(), entity))
                .isBookmark(bookmarkRepository.existsByUserAndArticles(currentUser.getCurrentUser(), entity))
                .createAt(entity.getCreateAt())
                .build();
    }
}
