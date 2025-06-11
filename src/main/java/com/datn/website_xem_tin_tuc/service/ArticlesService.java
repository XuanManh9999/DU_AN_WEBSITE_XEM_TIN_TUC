package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.request.ArticlesRequest;
import com.datn.website_xem_tin_tuc.dto.response.ArticlesResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticlesService {
    List<ArticlesResponseDTO> getAllArticles(int limit, int offset, String sortBy, String order, String title);
    ArticlesResponseDTO getArticleById(Long id);
    ArticlesResponseDTO createArticle(String title, String slug, String content, MultipartFile thumbnail, Long authorId, Long categoryId);
    ArticlesResponseDTO updateArticle(Long id, ArticlesRequest request, MultipartFile thumbnail);
    void deleteArticle(Long id);
    ArticlesResponseDTO getArticleBySlug(String slug);
}
