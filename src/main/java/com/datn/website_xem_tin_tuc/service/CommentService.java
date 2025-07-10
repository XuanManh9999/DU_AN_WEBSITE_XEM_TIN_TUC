package com.datn.website_xem_tin_tuc.service;


import com.datn.website_xem_tin_tuc.dto.request.CommentRequest;
import com.datn.website_xem_tin_tuc.dto.request.UpdateCommentRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommentResponse;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;

import java.util.List;

public interface CommentService {
    void createComment(CommentRequest request);
    List<CommentResponse> getCommentsByArticleSlug(String slug);
    void deleteComment(Long commentId);
    CommonResponse updateComment(Long commentId, UpdateCommentRequest updateCommentRequest);
}
