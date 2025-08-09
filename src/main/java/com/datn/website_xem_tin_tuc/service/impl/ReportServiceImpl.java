package com.datn.website_xem_tin_tuc.service.impl;

import com.datn.website_xem_tin_tuc.component.CurrentUser;
import com.datn.website_xem_tin_tuc.dto.response.*;
import com.datn.website_xem_tin_tuc.entity.ArticlesEntity;
import com.datn.website_xem_tin_tuc.entity.TagArticlesEntity;
import com.datn.website_xem_tin_tuc.repository.ArticlesRepository;
import com.datn.website_xem_tin_tuc.repository.BookmarkRepository;
import com.datn.website_xem_tin_tuc.repository.LikeRepository;
import com.datn.website_xem_tin_tuc.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ArticlesRepository articlesRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    @Override
    public List<ArticlesPerMonthDTO> getArticlesCountByMonth(int year) {
        List<Object[]> results = articlesRepository.countArticlesByMonth(year);

        // Map kết quả từ DB (tháng -> số lượng)
        Map<Integer, Long> resultMap = results.stream()
                .collect(Collectors.toMap(
                        r -> (Integer) r[0],
                        r -> (Long) r[1]
                ));

        // Tạo danh sách 12 tháng, nếu không có thì count = 0
        List<ArticlesPerMonthDTO> fullResult = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            fullResult.add(ArticlesPerMonthDTO.builder()
                    .month(month)
                    .count(resultMap.getOrDefault(month, 0L))
                    .build());
        }

        return fullResult;
    }

    @Override
    public CommonResponse getArticlesByDate(int year, int month, Integer day, String sortBy, String order, int page, int size, String keyword) {
        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ArticlesEntity> pageResult = articlesRepository.findByDateAndKeyword(year, month, day, keyword, pageable);

        List<ArticlesResponseDTO> data = pageResult.getContent().stream()
                .map(this::mapToDto)
                .toList();

        return CommonResponse.builder()
                .status(200)
                .message("Lấy danh sách bài viết theo thời gian")
                .data(data)
                .totalItems(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .currentPage(pageResult.getNumber() + 1)
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


}
