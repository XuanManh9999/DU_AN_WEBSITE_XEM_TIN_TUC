package com.datn.website_xem_tin_tuc.controller;

import com.datn.website_xem_tin_tuc.dto.request.CommentRequest;
import com.datn.website_xem_tin_tuc.dto.request.UpdateCommentRequest;
import com.datn.website_xem_tin_tuc.dto.response.CommonResponse;
import com.datn.website_xem_tin_tuc.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
        commentService.createComment(request);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Thêm bình luận thành công")
                .build());
    }

    @GetMapping("/article/{slug}")
    public ResponseEntity<?> getCommentsByArticle(@PathVariable String slug) {
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .data(commentService.getCommentsByArticleSlug(slug))
                .message("Lấy thông tin bình luận của bài viết thành công")
                .build());
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "commentId") Long commentId, @RequestBody UpdateCommentRequest updateCommentRequest) {
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .data(commentService.updateComment(commentId, updateCommentRequest))
                .message("Lấy thông tin bình luận của bài viết thành công")
                .build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(CommonResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Xóa bình luận thành công")
                .build());
    }
}
