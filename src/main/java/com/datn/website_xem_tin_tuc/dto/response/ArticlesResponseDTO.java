package com.datn.website_xem_tin_tuc.dto.response;

import com.datn.website_xem_tin_tuc.enums.Active;
import com.datn.website_xem_tin_tuc.enums.TypeArticles;
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
    private Active active;
    private String slug;
    private Integer view;
    private TypeArticles type;
    private Integer quantityLike;
    private Integer quantityBookmark;
    private UserResponseDTO author;
    private CategoryResponseDTO category;

}
