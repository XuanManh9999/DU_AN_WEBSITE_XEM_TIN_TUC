package com.datn.website_xem_tin_tuc.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesRequest {
    private String title;
    private String slug;
    private String content;
    private Long authorId;
    private Long categoryId;
}

