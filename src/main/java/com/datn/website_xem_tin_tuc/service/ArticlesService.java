package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.ArticlesRequest;
import com.datn.website_xem_tin_tuc.dto.response.ArticlesResponseDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.enums.TypeArticles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ArticlesService {
    Map<Long, Long> fetchAndResetViewCounts();
    void increaseView(Long articleId, String userKey);
    CommonResponse getAllArticles(int limit, int offset, String sortBy, String order, String title);
    ArticlesResponseDTO getArticleById(Long id);
    ArticlesResponseDTO createArticle(String title, String slug, String content, MultipartFile thumbnail, TypeArticles typeEnum, List<Long> tagIds, Long categoryId);
    ArticlesResponseDTO updateArticle(Long id, ArticlesRequest request, MultipartFile thumbnail);
    void deleteArticle(Long id);
    ArticlesResponseDTO getArticleBySlug(String slug);
    List<ArticlesResponseDTO> getArticleByCategoryId(Long categoryId);
}
