package com.datn.website_xem_tin_tuc.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long articleId;
    private Long parentId;
    private String content;
}
