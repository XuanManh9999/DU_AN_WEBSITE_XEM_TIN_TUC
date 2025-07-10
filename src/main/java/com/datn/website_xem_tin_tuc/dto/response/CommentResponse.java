package com.datn.website_xem_tin_tuc.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private String username;
    private Date createdAt;
    private List<CommentResponse> replies;
}
