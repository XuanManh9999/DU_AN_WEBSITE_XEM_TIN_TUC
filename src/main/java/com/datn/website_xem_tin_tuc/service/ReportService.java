package com.datn.website_xem_tin_tuc.service;

import com.datn.website_xem_tin_tuc.dto.response.ArticlesPerMonthDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

import java.util.List;

public interface ReportService {
    List<ArticlesPerMonthDTO> getArticlesCountByMonth(int year);
    CommonResponse getArticlesByDate(int year, int month, Integer day, String sortBy, String order, int page, int size, String keyword);
}
