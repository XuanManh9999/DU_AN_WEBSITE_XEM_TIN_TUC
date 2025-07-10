package com.datn.website_xem_tin_tuc.dto.request;

import com.datn.website_xem_tin_tuc.enums.TypeArticles;
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
    private TypeArticles type;
    private Long authorId;
    private Long categoryId;
}

