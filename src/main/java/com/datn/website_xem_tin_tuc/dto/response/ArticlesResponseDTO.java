package com.datn.website_xem_tin_tuc.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticlesResponseDTO {
    private Long id;
    private String title;
    private String content;
    private String thumbnail;
    private Integer view;
    private String author;
    private String category;
}
