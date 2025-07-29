package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeResponseDTO {
    private Long id;
    private String nameArticles;
    private String categoryName;
    private String content;
    private Integer view;
    private String slug;
    private String slugCategory;
    private String thumbnail;
    private Long ArticlesId;
    private Long userId;
    private String author;
    private Date createAt;
    private Date articlesCreateAt;
}
