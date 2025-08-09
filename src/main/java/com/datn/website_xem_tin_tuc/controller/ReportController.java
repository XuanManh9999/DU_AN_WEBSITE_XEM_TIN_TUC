package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.response.ArticlesPerMonthDTO;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/articles-by-month")
    public ResponseEntity<CommonResponse> getArticlesByMonth(@RequestParam int year) {
        List<ArticlesPerMonthDTO> result = reportService.getArticlesCountByMonth(year);
        return ResponseEntity.ok(
                CommonResponse.builder()
                        .status(200)
                        .message("Thống kê số lượng bài viết theo tháng")
                        .data(result)
                        .build()
        );
    }


    @GetMapping("/articles-detail-month")
    public ResponseEntity<CommonResponse> getArticlesByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) Integer day,
            @RequestParam(defaultValue = "createAt") String sortBy, // createAt or view
            @RequestParam(defaultValue = "desc") String order,       // asc or desc
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(
                reportService.getArticlesByDate(year, month, day, sortBy, order, page, size, keyword)
        );
    }

}
