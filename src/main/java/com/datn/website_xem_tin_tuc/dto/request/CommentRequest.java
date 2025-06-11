package com.datn.website_xem_tin_tuc.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private Long ArticlesId;
    private String content;
}
